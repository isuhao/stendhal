package games.stendhal.server.maps.quests;
import games.stendhal.server.StendhalRPRuleProcessor;
import games.stendhal.server.StendhalRPWorld;
import games.stendhal.server.entity.Player;
import games.stendhal.server.entity.item.StackableItem;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;

import java.util.ArrayList;
import java.util.List;

/** 
 * QUEST: Introduce new players to game
 * PARTICIPANTS:  * - Tad
 * - Margaret
 * - Ilisa
 * 
 * STEPS: 
 * - Tad asks you to buy a flask to give it to Margaret.
 * - Margaret sells you a flask
 * - Tad thanks you and asks you to take the flask to Ilisa
 * - Ilisa asks you for a few herbs.
 * - Return the created dress potion to Tad.
 *
 * REWARD: 
 * - 170 XP
 * - 10 gold coins 
 *
 * REPETITIONS:
 * - None.
 */
public class IntroducePlayers extends AbstractQuest {
	private static final String QUEST_SLOT = "introduce_players";

	@Override
	public void init(String name) {
		super.init(name, QUEST_SLOT);
	}

	@Override
	public List<String> getHistory(Player player) {
		List<String> res = new ArrayList<String>();
		if (player.hasQuest("TadFirstChat")) {
			res.add("FIRST_CHAT");
		}
		if (!player.hasQuest(QUEST_SLOT)) {
			return res;
		}
		String questState = player.getQuest(QUEST_SLOT);
		if (player.isQuestInState(QUEST_SLOT, "start", "ilisa", "corpse&herbs", "potion", "done")) {
			res.add("GET_FLASK");
		}
		if ((questState.equals("start") && player.isEquipped("flask")) || player.isQuestInState(QUEST_SLOT, "ilisa", "corpse&herbs", "potion", "done")) {
			res.add("GOT_FLASK");
		}
		if (player.isQuestInState(QUEST_SLOT, "ilisa", "corpse&herbs", "potion", "done")) {
			res.add("FLASK_TO_ILISA");
		}
		if (player.isQuestInState(QUEST_SLOT, "corpse&herbs", "potion", "done")) {
			res.add("GET_HERB");
		}
		if ((questState.equals("corpse&herbs") && player.isEquipped("arandula")) || player.isQuestInState(QUEST_SLOT, "potion", "done")) {
			res.add("GET_HERB");
		}
		if (player.isQuestInState(QUEST_SLOT, "potion", "done")) {
			res.add("TALK_TO_TAD");
		}
		if (questState.equals("done")) {
			res.add("DONE");
		}
		return res;
	}

	private void step_1() {		SpeakerNPC npc = npcs.get("Tad");
		npc.add(ConversationStates.ATTENDING,
				SpeakerNPC.QUEST_MESSAGES,
				null,
				ConversationStates.ATTENDING,
				null,
				new SpeakerNPC.ChatAction() {
					public void fire(Player player, String text, SpeakerNPC engine) {
						if (player.isQuestCompleted("introduce_players")) {
							engine.say("I have nothing for you now.");
						} else {
							engine.say("I need you to get a #flask from someone.");
						}
					}
				});
		/** In case Quest has already been completed */		npc.add(ConversationStates.ATTENDING,				"flask",				new SpeakerNPC.ChatCondition() {					public boolean fire(Player player, SpeakerNPC npc) {						return player.isQuestCompleted("introduce_players");					}				},				ConversationStates.ATTENDING,				"You already did the quest.",				null);
		/** If quest is not started yet, start it. */
		npc.add(ConversationStates.ATTENDING,
				"flask",
				new SpeakerNPC.ChatCondition() {
					public boolean fire(Player player, SpeakerNPC npc) {
						return !player.hasQuest("introduce_players");
					}
				},
				ConversationStates.QUEST_OFFERED,
				"Could you buy a flask from #Margaret?",
				null);
		npc.add(ConversationStates.QUEST_OFFERED,				"yes",				null,				ConversationStates.ATTENDING,				null,				new SpeakerNPC.ChatAction() {					public void fire(Player player, String text, SpeakerNPC engine) {						engine.say("Nice! Please hurry up!");						player.setQuest("introduce_players", "start");					}				});
		npc.add(ConversationStates.QUEST_OFFERED,
				"no",
				null,
				ConversationStates.ATTENDING,
				"Ok. But you should really consider it.|There is a nice reward :).",
				null);
		npc.add(ConversationStates.QUEST_OFFERED,				"margaret",				null,				ConversationStates.QUEST_OFFERED,				"Margaret is the tavern maid that work in Semos tavern. So will you do it?",				null);
		/** Remind player about the quest */
		npc.add(ConversationStates.ATTENDING,
				"flask",
				new SpeakerNPC.ChatCondition() {
					public boolean fire(Player player, SpeakerNPC npc) {
						return player.hasQuest("introduce_players")
								&& player.getQuest("introduce_players").equals("start")
								&& !player.isEquipped("flask");
					}
				},
				ConversationStates.ATTENDING,
				"I really need that #flask now! Go to talk with #Margaret.",
				null);
		npc.add(ConversationStates.ATTENDING,				"margaret",				null,				ConversationStates.ATTENDING,				"Margaret is the tavern maid that work in Semos tavern.",				null);	}
	private void step_2() {
		/** Just buy the stuff from Margaret. It isn't a quest */
	}
	private void step_3() {		SpeakerNPC npc = npcs.get("Tad");
    // staring the conversation the first time after getting a flask.
    npc.add(ConversationStates.IDLE,
				SpeakerNPC.GREETING_MESSAGES,
				new SpeakerNPC.ChatCondition() {
					public boolean fire(Player player, SpeakerNPC npc) {
						return player.hasQuest("introduce_players")
								&& player.getQuest("introduce_players").equals(
										"start") && player.isEquipped("flask");
					}
				},
				ConversationStates.ATTENDING,
				null,
				new SpeakerNPC.ChatAction() {
					public void fire(Player player, String text,
							SpeakerNPC engine) {
						engine.say("Ok!, I see you have the flask! Now I need you to take it to #ilisa");
						StackableItem money = (StackableItem) world
								.getRuleManager().getEntityManager().getItem(
										"money");
						money.setQuantity(10);
						player.equip(money);
						player.addXP(10);
						world.modify(player);
						player.setQuest("introduce_players", "ilisa");
					}
				});
    // remind the player to take the flask to ilisa.    npc.add(ConversationStates.IDLE,        SpeakerNPC.GREETING_MESSAGES,        new SpeakerNPC.ChatCondition() {            public boolean fire(Player player, SpeakerNPC npc) {                return player.hasQuest("introduce_players")                        && player.getQuest("introduce_players").equals(                                "ilisa") && player.isEquipped("flask");            }        },        ConversationStates.ATTENDING,        null,        new SpeakerNPC.ChatAction() {            public void fire(Player player, String text,                    SpeakerNPC engine) {                engine.say("Ok!, I see you have the flask! Now I need you to take it to #ilisa");            }        });
		npc.add(ConversationStates.ATTENDING,				"ilisa",				null,				ConversationStates.ATTENDING,				"Ilisa is the summon healer at Semos temple.",				null);	}
	private void step_4() {
		SpeakerNPC npc = npcs.get("Ilisa");
		npc.add(ConversationStates.IDLE,				SpeakerNPC.GREETING_MESSAGES,				new SpeakerNPC.ChatCondition() {					public boolean fire(Player player, SpeakerNPC npc) {						return player.hasQuest("introduce_players")								&& player.getQuest("introduce_players").equals(										"ilisa");					}				},				ConversationStates.ATTENDING,				null,				new SpeakerNPC.ChatAction() {					public void fire(Player player, String text,							SpeakerNPC engine) {						if (player.drop("flask")) {							engine.say("Thanks for the flask. Please, now I need a few #herbs to create the potion for #Tad.");							player.addXP(10);
							world.modify(player);
							player.setQuest("introduce_players", "corpse&herbs");						} else {							engine.say("Weren't you supposed to have a flask for me? Go and get a flask.");						}					}				});

        npc.add(ConversationStates.ATTENDING,
				"herbs",
				null,
				ConversationStates.ATTENDING,
				"North of Semos, near the tree grove, grows a herb called arandula.",
				null);
		
		npc.add(ConversationStates.ATTENDING,
				"tad",
				null,
				ConversationStates.ATTENDING,
				"He needs a very powerful potion to heal himself. He offers a good reward to anyone who will help him.",
				null);
	}
	private void step_5() {		SpeakerNPC npc = npcs.get("Ilisa");
		npc.add(ConversationStates.IDLE,
				SpeakerNPC.GREETING_MESSAGES,
				new SpeakerNPC.ChatCondition() {
					public boolean fire(Player player, SpeakerNPC npc) {
						return player.hasQuest("introduce_players")
								&& player.getQuest("introduce_players").equals(
										"corpse&herbs");
					}
				},
				ConversationStates.ATTENDING,
				null,
				new SpeakerNPC.ChatAction() {
					public void fire(Player player, String text,
							SpeakerNPC engine) {
						if (player.drop("arandula")) {
							engine.say("WOOOMMM!!! WUUOOAAAANNNN!!! WUUUUUNNN!!!| Tell #Tad that his potion is ready and that he should come here.");
							player.addXP(50);
							world.modify(player);
							player.setQuest("introduce_players", "potion");
						} else {
							engine.say("Don't have #herbs yet?");
						}
					}
				});
		npc.add(ConversationStates.ATTENDING,				"potion",				null,				ConversationStates.ATTENDING,				"The potion that #Tad is waiting for",				null);	}
	private void step_6() {
		SpeakerNPC npc = npcs.get("Tad");
		npc.add(ConversationStates.IDLE,				SpeakerNPC.GREETING_MESSAGES,				new SpeakerNPC.ChatCondition() {					public boolean fire(Player player, SpeakerNPC npc) {						return player.hasQuest("introduce_players")								&& player.getQuest("introduce_players").equals(										"potion");					}				},				ConversationStates.ATTENDING,				null,				new SpeakerNPC.ChatAction() {					public void fire(Player player, String text,							SpeakerNPC engine) {						engine.say("Thanks! I will go talk with Ilisa as soon as possible.");						player.addXP(100);						world.modify(player);
						player.setQuest("introduce_players", "done");
					}
				});
	}

	@Override
	public void addToWorld(StendhalRPWorld world, StendhalRPRuleProcessor rules) {
		super.addToWorld(world, rules);

		step_1();
		step_2();
		step_3();
		step_4();
		step_5();
		step_6();
	}
}