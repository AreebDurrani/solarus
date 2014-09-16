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

import org.solarus.editor.*;

/**
 * Represents a shop treasure that the hero can buy.
 */
public class ShopTreasure extends MapEntity {

    /**
     * Description of the default images representing this kind of entity.
     */
    public static final EntityImageDescription[] generalImageDescriptions =
    {new EntityImageDescription("entity_shop_treasure.png", 0, 0, 32, 32)};

    /**
     * Creates a new shop treasure.
     * @param map the map
     */
    public ShopTreasure(Map map) throws MapException {
        super(map, 32, 32);
    }

    /**
     * Declares all properties specific to the current entity type and sets
     * their initial values.
     */
    public void createProperties() throws MapException {
        createStringProperty("treasure_name", false, null);
        createIntegerProperty("treasure_variant", true, 1);
        createStringProperty("treasure_savegame_variable", true, null);
        createIntegerProperty("price", false, 0);
        createStringProperty("dialog", false, null);
    }

    /**
     * Checks the specific properties.
     * @throws MapException if a property is not valid
     */
    public void checkProperties() throws MapException {

        String treasureName = getStringProperty("treasure_name");
        if (treasureName == null) {
            throw new MapException("The treasure of a shop treasure cannot be empty");
        }

        checkTreasureProperties();

        int price = getIntegerProperty("price");
        if (price <= 0) {
            throw new MapException("The price must be positive");
        }

        String dialogId = getStringProperty("dialog");
        if (dialogId == null || dialogId.length() == 0) {
            throw new MapException("No description dialog defined");
        }
    }
}

