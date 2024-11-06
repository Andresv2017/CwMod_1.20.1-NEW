package net.andres.cassowarymod.items.custom;

import net.andres.cassowarymod.block.ModBlocks;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class FossilRadarItem  extends Item {
    public FossilRadarItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (!pContext.getLevel().isClientSide) {
            BlockPos positionClicked = pContext.getClickedPos();
            Player player = pContext.getPlayer();
            boolean foundFossil = false;

            // Bucle para buscar en el eje Y
            for (int i = 0; i <= 64; i++) {  // Limitar la búsqueda a 64 bloques hacia abajo
                BlockState stateY = pContext.getLevel().getBlockState(positionClicked.below(i));

                if (isValuableBlock(stateY)) {
                    outputValuableCoordinates(positionClicked.below(i), player, stateY.getBlock());
                    foundFossil = true;
                    break;
                }
            }

            // Bucle para buscar en el eje X (a la izquierda y derecha)
            if (!foundFossil) {
                for (int offsetX = -64; offsetX <= 64; offsetX++) {  // Limitar la búsqueda a 64 bloques a la izquierda y derecha
                    if (offsetX == 0) continue;  // Ignorar el mismo bloque

                    BlockState stateX = pContext.getLevel().getBlockState(positionClicked.offset(offsetX, 0, 0));

                    if (isValuableBlock(stateX)) {
                        outputValuableCoordinates(positionClicked.offset(offsetX, 0, 0), player, stateX.getBlock());
                        foundFossil = true;
                        break;
                    }
                }
            }

            if (!foundFossil) {
                player.sendSystemMessage(Component.literal("No se encontraron fósiles"));
            }
        }

        pContext.getItemInHand().hurtAndBreak(1, pContext.getPlayer(),
                player -> player.broadcastBreakEvent(player.getUsedItemHand()));

        return InteractionResult.SUCCESS;
    }


    private void outputValuableCoordinates(BlockPos blockPos, Player player, Block block) {
        int distanceY = Math.abs(player.blockPosition().getY() - blockPos.getY()) - 1; // Distancia en el eje Y
        int distanceX = Math.abs(player.blockPosition().getX() - blockPos.getX()) -1; // Distancia en el eje X

        String message = "Encontraste " + I18n.get(block.getDescriptionId());

        if (distanceY > 0) {
            message += " a " + distanceY + " bloques de distancia";
        } else if (distanceX > 0) {
            message += " a " + distanceX + " bloques de distancia";
        }

        player.sendSystemMessage(Component.literal(message));
    }


    private boolean isValuableBlock(BlockState state) {
        return state.is(ModBlocks.FOSSIL_BLOCK.get());
    }


}
