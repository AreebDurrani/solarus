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
package org.solarus.editor.map_editor_actions;

import java.awt.*;
import org.solarus.editor.*;
import org.solarus.editor.entities.*;

/**
 * Editing all properties of an entity : the common ones
 * (the name, the layer, the position, the size, the direction and the subtype)
 * and the specific ones (defined by an instance of ActionEditEntitySpecific).
 */
public class ActionEditEntity extends MapEditorAction {

    private MapEntity entity; // the entity modified

    // common data of the entity
    private String nameBefore;
    private String nameAfter;

    private Layer layerBefore;
    private Layer layerAfter;

    private Rectangle positionBefore;
    private Rectangle positionAfter;

    private int directionBefore;
    private int directionAfter;

    private EntitySubtype subtypeBefore;
    private EntitySubtype subtypeAfter;

    // additional action specific to each entity type
    private ActionEditEntitySpecific specificAction;

    /**
     * Constructor.
     * @param map the map
     * @param entity the entity edited
     * @param name the new name of the entity (or null if
     * the entity has no name)
     * @param layer the new layer of the entity
     * @param position the new position of the entity
     * @param size the new size of the entity (or null if
     * the entity is not resizable)
     * @param direction the new direction of the entity
     * (or -1 if the entity has no direction property)
     * @param subtype the subtype of the entity
     * (or null if the entity has no subtype property)
     * @param specificAction the information about the specific properties
     * of the entity
     */
    public ActionEditEntity(Map map, MapEntity entity, String name,
            Layer layer, Point position, Dimension size, int direction,
            EntitySubtype subtype, ActionEditEntitySpecific specificAction) {
        super(map);

        this.entity = entity;
        this.specificAction = specificAction;

        // name
        if (entity.canHaveName()) {
            this.nameBefore = entity.getName();
            this.nameAfter = name;
        }

        // layer
        this.layerBefore = entity.getLayer();
        this.layerAfter = layer;

        // position and size
        this.positionBefore = new Rectangle(entity.getX(), entity.getY(), 0, 0);
        this.positionAfter = new Rectangle(position.x, position.y, 0, 0);

        // size
        if (entity.isResizable()) {
            this.positionBefore.setSize(entity.getSize());
            this.positionAfter.setSize(size);
        }

        // direction
        if (entity.hasDirectionProperty()) {
            this.directionBefore = entity.getDirection();
            this.directionAfter = direction;
        }

        // subtype
        if (entity.hasSubtype()) {
            this.subtypeBefore = entity.getSubtype();
            this.subtypeAfter = subtype;
        }
    }

    /**
     * Executes the action.
     */
    public void execute() throws QuestEditorException {

        // name
        if (entity.canHaveName()) {
            entity.setName(nameAfter);
        }

        // layer
        map.setEntityLayer(entity, layerAfter);

        // position
        map.setEntityPosition(entity, positionAfter.x, positionAfter.y);

        // size
        if (entity.isResizable()) {
            map.setEntitySize(entity, positionAfter.width, positionAfter.height);
        }

        // direction
        if (entity.hasDirectionProperty()) {
            map.setEntityDirection(entity, directionAfter);
        }

        // subtype
        if (entity.hasSubtype()) {
            entity.setSubtype(subtypeAfter);
        }

        // specific data
        if (specificAction != null) {
            specificAction.execute();
        }

        entity.checkProperties();
        entity.updateImageDescription();
    }

    /**
     * Undoes the action.
     */
    public void undo() throws QuestEditorException {

        // specific data
        if (specificAction != null) {
            specificAction.undo();
        }

        // subtype
        if (entity.hasSubtype()) {
            entity.setSubtype(subtypeBefore);
        }

        // direction
        if (entity.hasDirectionProperty()) {
            map.setEntityDirection(entity, directionBefore);
        }

        // size
        if (entity.isResizable()) {
            // Restore the size even if it was invalid before.
            map.setEntitySizeUnchecked(entity, positionBefore.width, positionBefore.height);
        }

        // position
        // Restore the position even if it was invalid before.
        map.setEntityPositionUnchecked(entity, positionBefore.x, positionBefore.y);

        // layer
        map.setEntityLayer(entity, layerBefore);

        // name
        if (entity.canHaveName()) {
            entity.setName(nameBefore);
        }

        entity.updateImageDescription();
        // note that we don't check the properties: if the entity was
        // in an invalid state, let the action be undone anyway
    }
}
