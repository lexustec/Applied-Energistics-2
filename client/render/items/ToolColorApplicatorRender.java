package appeng.client.render.items;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import appeng.api.util.AEColor;
import appeng.client.texture.ExtraItemTextures;
import appeng.items.misc.ItemPaintBall;
import appeng.items.tools.powered.ToolColorApplicator;

public class ToolColorApplicatorRender implements IItemRenderer
{

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return helper == ItemRendererHelper.ENTITY_BOBBING || helper == ItemRendererHelper.ENTITY_ROTATION;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		IIcon par2Icon = item.getIconIndex();

		float f4 = ((IIcon) par2Icon).getMinU();
		float f5 = ((IIcon) par2Icon).getMaxU();
		float f6 = ((IIcon) par2Icon).getMinV();
		float f7 = ((IIcon) par2Icon).getMaxV();
		float f12 = 0.0625F;

		Tessellator tessellator = Tessellator.instance;
		GL11.glPushMatrix();
		GL11.glPushAttrib( GL11.GL_ALL_ATTRIB_BITS );

		int direction = 1;
		if ( type == ItemRenderType.INVENTORY )
		{
			GL11.glColor4f( 1, 1, 1, 1.0F );
			GL11.glScalef( 16F, 16F, 10F );
			GL11.glTranslatef( 0.0F, 1.0F, 0.0F );
			GL11.glRotatef( 180F, 1.0F, 0.0F, 0.0F );
			GL11.glEnable( GL11.GL_ALPHA_TEST );

			tessellator.startDrawingQuads();
			tessellator.setNormal( 0.0F, 1.0F, 0.0F );
			tessellator.addVertexWithUV( 0, 0, 0, (double) f4, (double) f7 );
			tessellator.addVertexWithUV( 1, 0, 0, (double) f5, (double) f7 );
			tessellator.addVertexWithUV( 1, 1, 0, (double) f5, (double) f6 );
			tessellator.addVertexWithUV( 0, 1, 0, (double) f4, (double) f6 );
			tessellator.draw();
		}
		else
		{
			if ( type == ItemRenderType.EQUIPPED_FIRST_PERSON )
				GL11.glTranslatef( 0.0F, 0.0F, 0.0F );
			else if ( type == ItemRenderType.EQUIPPED )
				GL11.glTranslatef( 0.0F, 0.0F, 0.0F );
			else
				GL11.glTranslatef( -0.5F, -0.3F, 0.01F );
			ItemRenderer.renderItemIn2D( tessellator, f5, f6, f4, f7, ((IIcon) par2Icon).getIconWidth(), ((IIcon) par2Icon).getIconHeight(), f12 );

			GL11.glDisable( GL11.GL_CULL_FACE );
			GL11.glColor4f( 1, 1, 1, 1.0F );
			GL11.glScalef( -1F, -1F, 1F );
			GL11.glTranslatef( -1.125F, 0.0f, f12 / -2.0f );
			GL11.glRotatef( 180F, 1.0F, 0.0F, 0.0F );
		}

		float u = ExtraItemTextures.White.getIcon().getInterpolatedU( 8.1 );
		float v = ExtraItemTextures.White.getIcon().getInterpolatedV( 8.1 );

		GL11.glScalef( 1F / 16F, 1F / 16F, 1F );
		GL11.glTranslatef( 4, 6, 0 );
		GL11.glDisable( GL11.GL_LIGHTING );

		ItemStack is = ((ToolColorApplicator) item.getItem()).getColor( item );
		if ( is != null && is.getItem() instanceof ItemPaintBall )
		{
			ItemPaintBall ipb = (ItemPaintBall) is.getItem();

			AEColor col = ipb.getColor( is );
			tessellator.startDrawingQuads();
			float z = 0;

			for (int g = 0; g < 7; g++)// 8
			{
				int x = g - 3 * direction;
				int y = g + 3 * direction;

				if ( type != ItemRenderType.INVENTORY )
				{
					x += 2;
				}

				if ( g < 6 )
				{
					tessellator.setColorOpaque_I( col.whiteVariant );
					tessellator.addVertexWithUV( x, y, z, (double) u, (double) v );
					tessellator.addVertexWithUV( x + 1, y, z, (double) u, (double) v );
					tessellator.addVertexWithUV( x + 1, y + 1, z, (double) u, (double) v );
					tessellator.addVertexWithUV( x, y + 1, z, (double) u, (double) v );
				}

				y -= direction;
				tessellator.setColorOpaque_I( col.mediumVariant );
				tessellator.addVertexWithUV( x, y, z, (double) u, (double) v );
				tessellator.addVertexWithUV( x + 1, y, z, (double) u, (double) v );
				tessellator.addVertexWithUV( x + 1, y + 1, z, (double) u, (double) v );
				tessellator.addVertexWithUV( x, y + 1, z, (double) u, (double) v );

				x += direction;
				if ( g < 6 )
				{
					tessellator.setColorOpaque_I( col.blackVariant );
					tessellator.addVertexWithUV( x, y, z, (double) u, (double) v );
					tessellator.addVertexWithUV( x + 1, y, z, (double) u, (double) v );
					tessellator.addVertexWithUV( x + 1, y + 1, z, (double) u, (double) v );
					tessellator.addVertexWithUV( x, y + 1, z, (double) u, (double) v );
				}
			}
			tessellator.draw();
		}

		GL11.glPopAttrib();
		GL11.glPopMatrix();
	}
}