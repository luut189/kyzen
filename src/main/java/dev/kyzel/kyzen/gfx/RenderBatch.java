package dev.kyzel.kyzen.gfx;

import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.engine.Window;
import dev.kyzel.kyzen.engine.components.SpriteComponent;
import dev.kyzel.kyzen.engine.SceneManager;
import dev.kyzel.kyzen.utils.AssetManager;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class RenderBatch implements Comparable<RenderBatch> {
    // Vertex
    // ====
    // Pos              Color                           Tex Coords          Tex ID
    // float, float     float, float, float, float      float, float        float

    private static final int POS_SIZE = 2;
    private static final int COLOR_SIZE = 4;
    private static final int TEX_COORS_SIZE = 2;
    private static final int TEX_ID_SIZE = 1;

    private static final int POS_OFFSET = 0;
    private static final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private static final int TEX_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private static final int TEX_ID_OFFSET = TEX_COORDS_OFFSET + TEX_COORS_SIZE * Float.BYTES;
    private static final int VERTEX_SIZE = POS_SIZE + COLOR_SIZE + TEX_COORS_SIZE + TEX_ID_SIZE;
    private static final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private final SpriteComponent[] sprites;
    private int numSprites;
    private boolean hasRoom;
    private final float[] vertices;
    private final int[] texSlots = {0, 1, 2, 3, 4, 5, 6, 7};

    private final List<Texture> textures;
    private int vaoID, vboID;
    private final int maxBatchSize;
    private final Shader shader;
    private final float zIndex;

    private final Framebuffer renderBuffer;

    public RenderBatch(int maxBatchSize, float zIndex) {
        shader = AssetManager.getShader("assets/shaders/default.glsl");
        sprites = new SpriteComponent[maxBatchSize];
        this.maxBatchSize = maxBatchSize;

        vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];
        numSprites = 0;
        hasRoom = true;
        textures = new ArrayList<>();
        this.zIndex = zIndex;

        renderBuffer = new Framebuffer(Window.get().getWidth(), Window.get().getHeight());
    }

    public void start() {
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        int eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, TEX_COORS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
        glEnableVertexAttribArray(3);
    }

    public void addSprite(SpriteComponent sprite) {
        sprites[numSprites] = sprite;

        if (sprite.getTexture() != null && !textures.contains(sprite.getTexture())) {
            textures.add(sprite.getTexture());
        }

        loadVertexProperties(numSprites);
        if (++numSprites >= maxBatchSize) {
            hasRoom = false;
        }
    }

    public boolean removeSprite(SpriteComponent sprite) {
        for (int i = 0; i < numSprites; i++) {
            if (sprites[i] == sprite) {
                if (i < numSprites - 1) {
                    sprites[i] = sprites[numSprites - 1];
                    loadVertexProperties(i);
                }
                sprites[numSprites - 1] = null;
                numSprites--;

                hasRoom = true;

                return true;
            }
        }
        return false;
    }


    public void render() {
        glViewport(0, 0,
                renderBuffer.getWidth(), renderBuffer.getHeight());
        renderToFramebuffer(renderBuffer);

        shader.detach();
    }

    private void renderToFramebuffer(Framebuffer framebuffer) {
        framebuffer.bind();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        boolean needReBuffer = false;
        for (int i = 0; i < numSprites; i++) {
            SpriteComponent sprite = sprites[i];
            if (sprite.isDirty()) {
                loadVertexProperties(i);
                sprite.setClean();
                needReBuffer = true;
            }
        }
        if (needReBuffer) {
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
        }

        shader.use();
        shader.uploadMatrix4f("uProjection", SceneManager.getCurrentScene().getCamera().getProjectionMatrix());
        shader.uploadMatrix4f("uView", SceneManager.getCurrentScene().getCamera().getViewMatrix());
        for (int i = 0; i < textures.size(); i++) {
            glActiveTexture(GL_TEXTURE0 + i + 1);
            textures.get(i).bind();
        }
        shader.uploadIntArray("uTextures", texSlots);

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);

        glDrawElements(GL_TRIANGLES, numSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);
        glBindVertexArray(0);

        for (Texture texture : textures) {
            texture.unbind();
        }

        framebuffer.unbind();
    }

    private void loadVertexProperties(int index) {
        SpriteComponent sprite = sprites[index];

        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = sprite.getColor();
        Vector2f[] texCoords = sprite.getTexCoords();
        int texID = 0;
        if (sprite.getTexture() != null) {
            for (int i = 0; i < textures.size(); i++) {
                if (textures.get(i) == sprite.getTexture()) {
                    texID = i + 1;
                    break;
                }
            }
        }

        float xAdd = 1.0f;
        float yAdd = 1.0f;
        for (int i = 0; i < 4; i++) {
            if (i == 1) {
                yAdd = 0.0f;
            } else if (i == 2) {
                xAdd = 0.0f;
            } else if (i == 3) {
                yAdd = 1.0f;
            }

            Transform transform = sprite.getOwner().getTransform();

            vertices[offset] =
                    transform.position.x + (xAdd * transform.scale.x);
            vertices[offset + 1] =
                    transform.position.y + (yAdd * transform.scale.y);

            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            vertices[offset + 6] = texCoords[i].x;
            vertices[offset + 7] = texCoords[i].y;

            vertices[offset + 8] = texID;

            offset += VERTEX_SIZE;
        }
    }

    private int[] generateIndices() {
        // 6 indices per quad (3 per triangle)
        int[] elements = new int[6 * maxBatchSize];
        for (int i = 0; i < maxBatchSize; i++) {
            loadElementIndices(elements, i);
        }
        return elements;
    }

    private void loadElementIndices(int[] elements, int index) {
        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;

        elements[offsetArrayIndex] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset;

        elements[offsetArrayIndex + 3] = offset;
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;
    }

    public Framebuffer getFramebuffer() {
        return renderBuffer;
    }

    public void resize(int width, int height) {
        renderBuffer.resize(width, height);
    }

    public boolean hasRoom() {
        return hasRoom;
    }

    public boolean hasTextureRoom() {
        return textures.size() < 8;
    }

    public boolean hasTexture(Texture texture) {
        return textures.contains(texture);
    }

    public float getZIndex() {
        return zIndex;
    }

    @Override
    public int compareTo(RenderBatch o) {
        return Float.compare(zIndex, o.getZIndex());
    }
}
