package games.stendhal.server.trade;


import games.stendhal.server.entity.PassiveEntity;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.player.Player;
import marauroa.common.game.RPClass;
import marauroa.common.game.RPLink;
import marauroa.common.game.RPObject;
import marauroa.common.game.Definition.Type;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Offer extends PassiveEntity {
	
	private static final String OFFERER_ATTRIBUTE_NAME = "offerer";

	public static final String OFFER_ITEM_SLOT_NAME = "item";

	public static final String OFFER_RPCLASS_NAME = "offer";

	private final Item item;
	
	private final Integer price;

	private final String offerer;
	
	public static void generateRPClass() {
		final RPClass offerRPClass = new RPClass(OFFER_RPCLASS_NAME);
		offerRPClass.isA("entity");
		offerRPClass.addAttribute("price", Type.INT);
		offerRPClass.addAttribute("class", Type.STRING);
		offerRPClass.addAttribute(OFFERER_ATTRIBUTE_NAME, Type.STRING);
		offerRPClass.addRPSlot(OFFER_ITEM_SLOT_NAME, 1);
	}
	
	/**
	 * @param item
	 */
	public Offer(final Item item, final Integer price, final String offerer) {
		super();
		setRPClass("offer");
		if (!this.hasSlot(OFFER_ITEM_SLOT_NAME)) {
			this.addSlot(OFFER_ITEM_SLOT_NAME);
		}
		this.getSlot(OFFER_ITEM_SLOT_NAME).add(item);
		this.item = item;
		this.put("price", price.intValue());
		this.price = price;
		this.put(OFFERER_ATTRIBUTE_NAME, offerer);
		this.offerer = offerer;
		store();
	}
	
	public Offer(final RPObject object) {
		this((Item) object.getSlot(OFFER_ITEM_SLOT_NAME).getFirst(), Integer.valueOf(object.getInt("price")), object.get(OFFERER_ATTRIBUTE_NAME));
	}


	public final Item getItem() {
		return item;
	}



	public final Integer getPrice() {
		return price;
	}



	public final String getOfferer() {
		return offerer;
	}

	
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(final Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj, false,
				Offer.class);
	}
	
}
