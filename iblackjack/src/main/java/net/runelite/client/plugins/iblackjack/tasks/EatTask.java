package net.runelite.client.plugins.iblackjack.tasks;

import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.MenuEntry;
import net.runelite.api.MenuOpcode;
import net.runelite.api.Skill;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.plugins.iblackjack.Task;
import static net.runelite.client.plugins.iblackjack.iBlackjackPlugin.eatHP;
import static net.runelite.client.plugins.iblackjack.iBlackjackPlugin.timeout;

@Slf4j
public class EatTask extends Task
{
	Set<String> foodMenu = Set.of("Eat", "Drink");

	@Override
	public boolean validate()
	{
		return client.getBoostedSkillLevel(Skill.HITPOINTS) <= eatHP &&
			!isShopOpen() && inventory.getItemMenu(foodMenu) != null;
	}

	@Override
	public String getTaskDescription()
	{
		return status;
	}

	@Override
	public void onGameTick(GameTick event)
	{
		status = "Restoring HP";
		WidgetItem food = inventory.getItemMenu(foodMenu);
		if (food != null)
		{
			entry = new MenuEntry("", "", food.getId(), MenuOpcode.ITEM_FIRST_OPTION.getId(),
				food.getIndex(), WidgetInfo.INVENTORY.getId(), false);
			utils.doActionMsTime(entry, food.getCanvasBounds(), (int) sleepDelay());
			eatHP = calc.getRandomIntBetweenRange(config.minEatHP(), config.maxEatHP());
			timeout = tickDelay();
		}
		log.debug(status);
	}
}