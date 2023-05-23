package team.teampotato.MineGPT.Client.GUI;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import team.teampotato.MineGPT.Common.Config;

public class PingGUI implements ClientModInitializer {

    public static class GUI extends Screen {
        public GUI() {
            super(Text.literal("PingGUI"));
        }

        @Override
        protected void init() {
            super.init();

            // 添加两个按钮
            int buttonWidth = 100; //宽度
            int buttonHeight = 20; //高度
            int centerX = (this.width) / 2;
            int centerY = (this.height) / 2;
            this.addCustomButton(new ButtonWidget(centerX, centerY, buttonWidth, buttonHeight, Text.translatable("MultiuniverseEonAdventureCore.Disk.SafeMode.YES"), button -> {
                assert this.client != null;
                this.client.setScreen(null); // 返回

            }));
        }
        @Override
        public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            this.renderBackground(matrices);
            super.render(matrices, mouseX, mouseY, delta);
            Text text = Text.translatable("minegpt.gui.ping.title", Config.ENDPOINT,Ping.status);
            int textX = (this.width - this.textRenderer.getWidth(text)) / 2;
            int textY = this.height / 4;
            this.textRenderer.drawWithShadow(matrices, text, textX, textY, 0xFFFFFF);
        }

        public void addCustomButton(ButtonWidget button) {
            this.addDrawableChild(button);
        }

    }
    @Override
    public void onInitializeClient() {
    }
}

