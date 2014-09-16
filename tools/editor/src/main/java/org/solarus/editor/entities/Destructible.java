/*
 * Copyright (C) 2006-2014 Christopho, Solarus - http://www.solarus-games.org
 *
 * Solarus Quest Editor is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Solarus Quest Editor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.solarus.editor.entities;

import java.awt.*;

import org.solarus.editor.*;

/**
 * Represents an entity that Link can destroy (lift and throw or cut)
 * and that can hide a treasure.
 */
public class Destructible extends MapEntity {

    /**
     * Description of the default image representing this kind of entity.
     */
    public static final EntityImageDescription[] generalImageDescriptions = {
        new EntityImageDescription("entity_destructible.png", 0, 0, 32, 32)
    };

    /**
     * Origin point of this entity.
     */
    private static final Point origin = new Point(8, 13);

    /**
     * Creates a new destructible.
     * @param map the map
     */
    public Destructible(Map map) throws MapException {
        super(map, 16, 16);
    }

    /**
     * Returns the coordinates of the origin point of the entity.
     * @return the coordinates of the origin point of the entity
     */
    protected Point getOrigin() {
        return origin;
    }

    /**
     * Declares all properties specific to the current entity type and sets
     * their initial values.
     */
    public void createProperties() throws MapException {
        createStringProperty("treasure_name", true, null);
        createIntegerProperty("treasure_variant", true, 1);
        createStringProperty("treasure_savegame_variable", true, null);
        createStringProperty("sprite", false, "");
        createStringProperty("destruction_sound", true, null);
        createIntegerProperty("weight", true, 0);
        createBooleanProperty("can_be_cut", true, false);
        createBooleanProperty("can_explode", true, false);
        createBooleanProperty("can_regenerate", true, false);
        createIntegerProperty("damage_on_enemies", true, 1);
        createStringProperty("ground", true, Ground.WALL.getName());
    }

    /**
     * Notifies this entity that a property specific to its type has just changed.
     * Does nothing by default.
     * @param name Name of the property that has changed.
     * @param value The new value.
     * @throws MapException if sprite cannot be loaded.
     */
    @Override
    protected void notifyPropertyChanged(String name, String value) throws MapException {

        if (name.equals("sprite")) {

            if (isValidSpriteName(value)) {
                try {
                    setSprite(new Sprite(value, getMap()));
                } catch (SpriteException ex) {
                    throw new MapException(ex.getMessage());
                }
            }
            else {
                setSprite(null);
            }
        }
    }

    /**
     * Checks the specific properties.
     * @throws MapException if a property is not valid
     */
    public void checkProperties() throws MapException {

        checkTreasureProperties();

        String spriteName = getStringProperty("sprite");
        if (!isValidSpriteName(spriteName)) {
            throw new MapException("Invalid sprite name: '" + spriteName + "'");
        }
    }
}

