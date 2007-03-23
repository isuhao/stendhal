package games.stendhal.server.script;

import java.util.List;

import games.stendhal.server.StendhalRPRuleProcessor;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.scripting.ScriptImpl;

/**
 * Alters the state of a quest of a player.
 *
 * @author hendrik
 */
public class AlterQuest extends ScriptImpl {

	@Override
	public void execute(Player admin, List<String> args) {

		// help text
		if (args.size() < 2) {
			admin
			        .sendPrivateText("Usage /script AlterQuest.class <player> <questname> <state>. Ommit <state> to remove the quest.");
			return;
		}

		// find player
		StendhalRPRuleProcessor rules = StendhalRPRuleProcessor.get();
		Player target = rules.getPlayer(args.get(0));
		if (target != null) {

			// old state
			String questName = args.get(1);
			String oldQuestState = null;
			if (target.hasQuest(questName)) {
				oldQuestState = target.getQuest(questName);
			}

			// new state (or null to remove the quest)
			String newQuestState = null;
			if (args.size() > 2) {
				newQuestState = args.get(2);
			}

			// set the quest
			target.setQuest(questName, newQuestState);

			// notify admin and altered player
			target.sendPrivateText("Admin " + admin.getName() + " changed your state of the quest '" + questName
			        + "' from '" + oldQuestState + "' to '" + newQuestState + "'");
			admin.sendPrivateText("Changed the state of quest '" + questName + "' from '" + oldQuestState + "' to '"
			        + newQuestState + "'");
		} else {
			admin.sendPrivateText(args.get(0) + " is not logged in");
		}
	}

}
