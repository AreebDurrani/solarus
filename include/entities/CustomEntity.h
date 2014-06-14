/*
 * Copyright (C) 2006-2014 Christopho, Solarus - http://www.solarus-games.org
 * 
 * Solarus is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Solarus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */
#ifndef SOLARUS_CUSTOM_ENTITY_H
#define SOLARUS_CUSTOM_ENTITY_H

#include "Common.h"
#include "entities/Detector.h"
#include <map>

namespace solarus {

/**
 * \brief A map entity fully controlled by Lua.
 *
 * A custom entity may have a model, which is the name of a Lua script
 * that controls it. This is useful to make several custom entities with the
 * same behavior more easily.
 */
class CustomEntity: public Detector {

  public:

    CustomEntity(
        Game& game,
        const std::string& name,
        int direction,
        Layer layer,
        int x,
        int y,
        int width,
        int height,
        const std::string& sprite_name,
        const std::string& model);
    ~CustomEntity();

    EntityType get_type() const;

    const std::string& get_model() const;

    // Game loop.
    void notify_creating();
    void set_suspended(bool suspended);
    void notify_enabled(bool enabled);
    void update();
    void draw_on_map();

    // Direction.
    int get_sprites_direction() const;
    void set_sprites_direction(int direction);

    // What can traverse this custom entity.
    bool is_traversable_by_entity(MapEntity& entity);
    void set_traversable_by_entities(bool traversable);
    void set_traversable_by_entities(int traversable_test_ref);
    void reset_traversable_by_entities();
    void set_traversable_by_entities(EntityType type, bool traversable);
    void set_traversable_by_entities(EntityType type, int traversable_test_ref);
    void reset_traversable_by_entities(EntityType type);

    bool can_be_obstacle() const;
    bool is_obstacle_for(MapEntity& other);

    // What this custom entity can traverse.
    void set_can_traverse_entities(bool traversable);
    void set_can_traverse_entities(int traversable_test_ref);
    void reset_can_traverse_entities();
    void set_can_traverse_entities(EntityType type, bool traversable);
    void set_can_traverse_entities(EntityType type, int traversable_test_ref);
    void reset_can_traverse_entities(EntityType type);

    bool is_hero_obstacle(Hero& hero);
    bool is_block_obstacle(Block& block);
    bool is_teletransporter_obstacle(Teletransporter& teletransporter);
    bool is_stream_obstacle(Stream& stream);
    bool is_stairs_obstacle(Stairs& stairs);
    bool is_sensor_obstacle(Sensor& sensor);
    bool is_switch_obstacle(Switch& sw);
    bool is_raised_block_obstacle(CrystalBlock& raised_block);
    bool is_crystal_obstacle(Crystal& crystal);
    bool is_npc_obstacle(Npc& npc);
    bool is_enemy_obstacle(Enemy& enemy);
    bool is_jumper_obstacle(Jumper& jumper, const Rectangle& candidate_position);
    bool is_destructible_obstacle(Destructible& destructible);
    bool is_separator_obstacle(Separator& separator);

    bool can_traverse_ground(Ground ground) const;
    void set_can_traverse_ground(Ground ground, bool traversable);
    void reset_can_traverse_ground(Ground ground);

    bool is_low_wall_obstacle() const;
    bool is_shallow_water_obstacle() const;
    bool is_deep_water_obstacle() const;
    bool is_hole_obstacle() const;
    bool is_lava_obstacle() const;
    bool is_prickle_obstacle() const;
    bool is_ladder_obstacle() const;

    // Collisions.
    void add_collision_test(CollisionMode collision_test, int callback_ref);
    void add_collision_test(int collision_test_ref, int callback_ref);
    void clear_collision_tests();

    bool test_collision_custom(MapEntity& entity);
    void notify_collision(
        MapEntity& entity_overlapping, CollisionMode collision_mode);
    void notify_collision(
        MapEntity& other_entity, Sprite& other_sprite, Sprite& this_sprite);

    virtual void notify_collision_with_destructible(Destructible& destructible, CollisionMode collision_mode);
    virtual void notify_collision_with_teletransporter(Teletransporter& teletransporter, CollisionMode collision_mode);
    virtual void notify_collision_with_stream(Stream& stream, int dx, int dy);
    virtual void notify_collision_with_stairs(Stairs& stairs, CollisionMode collision_mode);
    virtual void notify_collision_with_jumper(Jumper& jumper, CollisionMode collision_mode);
    void notify_collision_with_sensor(
        Sensor& sensor, CollisionMode collision_mode);
    virtual void notify_collision_with_switch(Switch& sw, CollisionMode collision_mode);
    virtual void notify_collision_with_switch(Switch& sw, Sprite& sprite_overlapping);
    virtual void notify_collision_with_crystal(Crystal& crystal, CollisionMode collision_mode);
    virtual void notify_collision_with_crystal(Crystal& crystal, Sprite& sprite_overlapping);
    virtual void notify_collision_with_chest(Chest& chest);
    virtual void notify_collision_with_block(Block& block);
    virtual void notify_collision_with_separator(Separator& separator, CollisionMode collision_mode);
    virtual void notify_collision_with_bomb(Bomb& bomb, CollisionMode collision_mode);
    virtual void notify_collision_with_explosion(Explosion& explosion, CollisionMode collision_mode);
    virtual void notify_collision_with_explosion(Explosion& explosion, Sprite& sprite_overlapping);
    virtual void notify_collision_with_fire(Fire& fire, Sprite& sprite_overlapping);
    virtual void notify_collision_with_enemy(Enemy& enemy);
    virtual void notify_collision_with_enemy(Enemy& enemy, Sprite& enemy_sprite, Sprite& this_sprite);
    bool notify_action_command_pressed();
    bool interaction_with_item(EquipmentItem& item);

    // Observing the ground.
    bool is_ground_observer() const;
    void notify_ground_below_changed();

    // Modifying the ground.
    bool is_ground_modifier() const;
    Ground get_modified_ground() const;
    void set_modified_ground(Ground modified_ground);

  private:

    /**
     * \brief Stores whether a custom entity can be traversed by or can traverse
     * other entities.
     */
    class TraversableInfo {

      public:

        TraversableInfo();
        TraversableInfo(CustomEntity& entity, bool traversable);
        TraversableInfo(CustomEntity& entity, int traversable_test_ref);
        TraversableInfo(const TraversableInfo& other);
        ~TraversableInfo();

        TraversableInfo& operator=(const TraversableInfo& other);

        bool is_empty() const;
        bool is_traversable(MapEntity& other_entity) const;

      private:

        CustomEntity* entity;            /**< The custom entity.
                                        * NULL means no info. */
        int traversable_test_ref;        /**< Lua ref to a boolean function
                                        * that decides, or LUA_REFNIL. */
        bool traversable;                /**< Traversable property (unused if
                                        * there is a Lua function). */

    };

    /**
     * \brief Stores a callback to be executed when the specified test
     * detects a collision.
     */
    class CollisionInfo {

      public:

        CollisionInfo();
        CollisionInfo(CustomEntity& entity,
            CollisionMode built_in_test, int callback_ref);
        CollisionInfo(CustomEntity& entity,
            int custom_test_ref, int callback_ref);
        CollisionInfo(const CollisionInfo& other);
        ~CollisionInfo();

        CollisionInfo& operator=(const CollisionInfo& other);

        CollisionMode get_built_in_test() const;
        int get_custom_test_ref() const;
        int get_callback_ref() const;

      private:

        CustomEntity* entity;            /**< The custom entity.
                                          * NULL means no info. */

        CollisionMode built_in_test;     /**< A built-in collision test
                                          * or COLLISION_CUSTOM. */
        int custom_test_ref;             /**< Ref to a custom collision test
                                          * or LUA_REFNIL. */
        int callback_ref;                /**< Ref to the function to called when
                                          * a collision is detected. */

    };

    const TraversableInfo& get_traversable_by_entity_info(EntityType type);
    const TraversableInfo& get_can_traverse_entity_info(EntityType type);

    void notify_collision_from(MapEntity& other_entity);
    void notify_collision_from(MapEntity& other_entity, Sprite& other_sprite, Sprite& this_sprite);

    std::string model;                                /**< Model of this custom entity or an empty string. */

    // Obstacles.

    TraversableInfo traversable_by_entities_general;  /**< Whether entities can traverse me by default or NULL. */
    std::map<EntityType, TraversableInfo>
        traversable_by_entities_type;                 /**< Whether entities of a type can traverse me. */

    TraversableInfo can_traverse_entities_general;    /**< Whether I can traverse entities by default or NULL. */
    std::map<EntityType, TraversableInfo>
        can_traverse_entities_type;                   /**< Whether I can traverse entities of a type. */
    std::map<Ground, bool> can_traverse_grounds;      /**< Whether I can traverse each kind of ground. */

    // Collisions.

    std::vector<CollisionInfo>
        collision_tests;               /**< The collision tests to perform. */
    std::vector<CollisionInfo>
        successful_collision_tests;   /**< Collision test that detected
                                        * collisions other than
                                        * COLLISION_SPRITE. */

    bool ground_modifier;              /**< Whether this custom entity is a ground modifier. */
    Ground modified_ground;            /**< The ground defined by this custom
                                        * entity or GROUND_EMPTY. */
};

}

#endif

