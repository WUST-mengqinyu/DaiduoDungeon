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
package com.daiduo.lightning.sprites;

import android.graphics.RectF;
import android.widget.Switch;

import com.daiduo.lightning.Assets;
import com.daiduo.lightning.Dungeon;
import com.daiduo.lightning.actors.hero.Hero;
import com.daiduo.lightning.actors.hero.HeroClass;
import com.daiduo.lightning.messages.Messages;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;

import java.security.KeyStore;

import static com.daiduo.lightning.actors.hero.HeroClass.WARRIOR;
import static com.daiduo.lightning.sprites.ItemSprite.film;

public class HeroSprite extends CharSprite {

	private static final int FRAME_WIDTH	= 14;
	private static final int FRAME_HEIGHT	= 15;
	
	private static final int RUN_FRAMERATE	= 20;
	
	private static TextureFilm tiers;
	
	private Animation fly;
	private Animation read;

	public HeroSprite() {
		super();
		
		link( Dungeon.hero );
		
		texture( Dungeon.hero.heroClass.spritesheet() );
		updateArmor();

		if (ch.isAlive())
			idle();
		else
			die();
	}
	
	public void updateArmor() {

		TextureFilm film = new TextureFilm( tiers(), ((Hero)ch).tier(), FRAME_WIDTH, FRAME_HEIGHT );


				idle = new Animation( 1, true );
                idle.frames( film, 0, 0, 0, 1, 0, 0, 1, 1 );

                run = new Animation( RUN_FRAMERATE, true );
		        switch(Dungeon.hero.heroClass) {
		        	case WARRIOR:
					case HUNTRESS:
		        	case MAGE:
		        		run.frames(film, 2, 3, 4, 5, 6, 7);
		        		break;
					case ROGUE:
						run.frames(film, 2, 3, 4, 5, 6);
						break;
		        }

                die = new Animation( 20, false );
		        switch(Dungeon.hero.heroClass) {
			        case WARRIOR:
				        die.frames(film, 8, 9, 10, 11, 12, 11);
				        break;
			        case ROGUE:
				        die.frames(film, 7, 8, 9, 10, 9);
				        break;
			        case HUNTRESS:
				        die.frames(film, 8, 9, 10, 11, 12, 11);
				        break;
			        case MAGE:
				        die.frames(film, 8, 9, 10, 11, 12, 11);
				        break;
		        }

				attack = new Animation(15, false);
				attack.frames(film, 13, 14, 15, 0);

                zap = attack.clone();

                operate = new Animation( 8, false );
                operate.frames( film, 16, 17, 16, 17 );

                fly = new Animation( 1, true );


		        switch(Dungeon.hero.heroClass) {
		        	case WARRIOR:
		        	case HUNTRESS:
		        	case MAGE:
		        		fly.frames( film, 18);
		        		break;
		        	case ROGUE:
		        		fly.frames( film,17);
		        		break;
		        }
                read = new Animation( 20, false );
	            switch(Dungeon.hero.heroClass) {
	            	case HUNTRESS:
	            	case MAGE:
	            	case WARRIOR:
	            		read.frames(film, 19, 20, 20, 20, 20, 20, 20, 20, 20, 19);
	            		break;
	            	case ROGUE:
	            		read.frames(film, 18, 19, 19, 19, 19, 19, 19, 19, 19, 18);
	            		break;

				}
	}
	
	@Override
	public void place( int p ) {
		super.place( p );
		Camera.main.target = this;
	}

	@Override
	public void move( int from, int to ) {
		super.move( from, to );
		if (ch.flying) {
			play( fly );
		}
		Camera.main.target = this;
	}

	@Override
	public void jump( int from, int to, Callback callback ) {
		super.jump( from, to, callback );
		play( fly );
	}

	public void read() {
		animCallback = new Callback() {
			@Override
			public void call() {
				idle();
				ch.onOperateComplete();
			}
		};
		play( read );
	}

	@Override
	public void bloodBurstA(PointF from, int damage) {
		//Does nothing.

		/*
		 * This is both for visual clarity, and also for content ratings regarding violence
		 * towards human characters. The heroes are the only human or human-like characters which
		 * participate in combat, so removing all blood associated with them is a simple way to
		 * reduce the violence rating of the game.
		 */
	}

	@Override
	public void update() {
		sleeping = ch.isAlive() && ((Hero)ch).resting;
		
		super.update();
	}
	
	public boolean sprint( boolean on ) {
		run.delay = on ? 0.667f / RUN_FRAMERATE : 1f / RUN_FRAMERATE;
		return on;
	}
	
	public static TextureFilm tiers() {
		if (tiers == null) {
			SmartTexture texture = TextureCache.get( Assets.ROGUE );
			tiers = new TextureFilm( texture, texture.width, FRAME_HEIGHT );
		}
		
		return tiers;
	}
	
	public static Image avatar( HeroClass cl, int armorTier ) {
		
		RectF patch = tiers().get( armorTier );
		Image avatar = new Image( cl.spritesheet() );
		RectF frame = avatar.texture.uvRect( 1, 0, FRAME_WIDTH, FRAME_HEIGHT );
		frame.offset( patch.left, patch.top );
		avatar.frame( frame );
		
		return avatar;
	}
}
