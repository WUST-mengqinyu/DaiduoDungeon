/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.daiduo.lightning.items.potions;

import com.daiduo.lightning.Dungeon;
import com.daiduo.lightning.actors.buffs.Bleeding;
import com.daiduo.lightning.actors.buffs.Buff;
import com.daiduo.lightning.actors.buffs.Cripple;
import com.daiduo.lightning.actors.buffs.Poison;
import com.daiduo.lightning.actors.buffs.Weakness;
import com.daiduo.lightning.actors.hero.Hero;
import com.daiduo.lightning.effects.Speck;
import com.daiduo.lightning.messages.Messages;
import com.daiduo.lightning.utils.GLog;

public class PotionOfHealing extends Potion {

	{
		initials = 2;

		bones = true;
	}
	
	@Override
	public void apply( Hero hero ) {
		setKnown();
		heal( Dungeon.hero );
		GLog.p( Messages.get(this, "heal") );
	}
	
	public static void heal( Hero hero ) {
		
		hero.HP = hero.HT;
		Buff.detach( hero, Poison.class );
		Buff.detach( hero, Cripple.class );
		Buff.detach( hero, Weakness.class );
		Buff.detach( hero, Bleeding.class );
		
		hero.sprite.emitter().start( Speck.factory( Speck.HEALING ), 0.4f, 4 );
	}

	@Override
	public int price() {
		return isKnown() ? 30 * quantity : super.price();
	}
}
