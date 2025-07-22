package it.mitl.maleficium.event.species.mainevents;

import it.mitl.maleficium.subroutine.VariableManager;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class KarmaEvents {

    @SubscribeEvent
    public static void onEntityKill(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {

            // if the entity is passive,
            if (!(event.getEntity() instanceof Monster)) {
                if (event.getEntity() instanceof Villager && ((Villager) event.getEntity()).isBaby()) {
                    int randomAmount = (int) (Math.random() * 5) + 3; // random value between 3 and 5
                    decreaseKarma(player, randomAmount);
                } else if (event.getEntity() instanceof Villager) {
                    int randomAmount = (int) (Math.random() * 3) + 1; // random value between 1 and 3
                    decreaseKarma(player, randomAmount);
                } else if (event.getEntity().isBaby()) {
                    int randomAmount = (int) (Math.random() * 1) + 5; // random value between 1 and 5
                    decreaseKarma(player, randomAmount);
                } else if (event.getEntity() instanceof TamableAnimal tamableAnimal) {
                    if (tamableAnimal.isTame() && tamableAnimal.getOwner() != null) {
                        int randomAmount = (int) (Math.random() * 3) + 1; // random value between 1 and 3
                        decreaseKarma(player, randomAmount);
                    }
                }
                return;
            }
            if (event.getEntity() instanceof Monster) {
                if (event.getEntity() instanceof Spider spider) {
                    if (spider.isAggressive()) {
                        int randomAmount = (int) (Math.random() * 3) + 1; // random value between 1 and 3
                        increaseKarma(player, randomAmount);
                    }
                    return;
                } else if (event.getEntity() instanceof ZombieVillager) {
                    if (((ZombieVillager) event.getEntity()).isConverting()) {
                        int randomAmount = (int) (Math.random() * 5) + 3; // random value between 3 and 5
                        decreaseKarma(player, randomAmount);
                        return;
                    }
                    int randomAmount = (int) (Math.random() * 2) + 1; // random value between 1 and 2
                    increaseKarma(player, randomAmount);
                    return;
                }
                else {
                    int randomAmount = (int) (Math.random() * 3) + 1; // random value between 1 and 3
                    increaseKarma(player, randomAmount);
                }
            }

        }
    }

    public static void increaseKarma(Player player, int amount) {
        int oldKarma = VariableManager.getKarma(player);
        int newKarma = oldKarma + amount;
        VariableManager.setKarma(player, newKarma);
    }

    public static void decreaseKarma(Player player, int amount) {
        int oldKarma = VariableManager.getKarma(player);
        int newKarma = oldKarma - amount;
        VariableManager.setKarma(player, newKarma);
    }
}
