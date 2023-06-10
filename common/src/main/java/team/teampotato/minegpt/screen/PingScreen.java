package team.teampotato.minegpt.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.joml.Matrix4f;
import team.teampotato.minegpt.config.Config;

@Environment(EnvType.CLIENT)
public class PingScreen extends Screen {
    private static final Text PING_SCREEN_TITLE = Text.literal("PingGUI");
    private static final Text Disk_SafeMode_YES_BUTTON_TEXT = Text.translatable("MultiuniverseEonAdventureCore.Disk.SafeMode.YES");
    public PingScreen() {
        super(PING_SCREEN_TITLE);
    }
    @Override
    protected void init() {
        super.init();

        // 添加两个按钮
        int buttonWidth = 100; //宽度
        int buttonHeight = 20; //高度
        int centerX = (this.width) / 2;
        int centerY = (this.height) / 2;
        this.addCustomButton(ButtonWidget.builder(Disk_SafeMode_YES_BUTTON_TEXT, (button) -> {
            assert this.client != null;
            this.client.setScreen(null); // 返回
        }).dimensions(centerX, centerY, buttonWidth, buttonHeight).build());
    }
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        Text text = Text.translatable("minegpt.gui.ping.title", Config.ENDPOINT, Ping.status);
        int textX = (this.width - this.textRenderer.getWidth(text)) / 2;
        int textY = this.height / 4;
        //this.textRenderer.drawWithShadow(matrices, text, textX, textY, 0xFFFFFF);
        this.textRenderer.drawWithOutline(text, textX, textY, 0, 0xFFFFFF, );
    }

    public void addCustomButton(ButtonWidget button) {
        this.addDrawableChild(button);
    }
}
