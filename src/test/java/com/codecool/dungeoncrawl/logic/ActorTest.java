package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;
import com.codecool.dungeoncrawl.logic.items.Coin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Actor")
class ActorTest {
    GameMap gameMap = new GameMap(3, 3, CellType.FLOOR);

    @Nested
    @DisplayName("move when")
    class MoveTest {

        @DisplayName("floor cell")
        @Test
        void moveUpdatesCells() {
            Player player = new Player(gameMap.getCell(1, 1));
            if (player.validateMove(1, 0)) {
                player.move(1, 0);
            }
            assertEquals(2, player.getX());
            assertEquals(1, player.getY());
            assertNull(gameMap.getCell(1, 1).getActor());
            assertEquals(player, gameMap.getCell(2, 1).getActor());
        }

        @DisplayName("open-door")
        @Test
        void movesOnOpenDoor() {
            gameMap.getCell(2, 1).setType(CellType.DOOR);
            Player player = new Player(gameMap.getCell(1, 1));
            if (player.validateMove(1, 0)) {
                player.move(1, 0);
            }
            assertEquals(2, player.getX());
            assertEquals(1, player.getY());
            assertNull(gameMap.getCell(1, 1).getActor());
            assertEquals(player, gameMap.getCell(2, 1).getActor());
        }

        @Nested
        @DisplayName("stairs when")
        class StairsTest {
            @Nested
            @DisplayName("actor is player")
            class StairsPlayerTest {
                @DisplayName("stairs up")
                @Test
                void stairsUpPlayer() {
                    gameMap.getCell(2, 1).setType(CellType.STAIRS_UP);
                    Player player = new Player(gameMap.getCell(1, 1));
                    if (player.validateMove(1, 0)) {
                        player.move(1, 0);
                    }
                    assertEquals(2, player.getX());
                    assertEquals(1, player.getY());
                    assertNull(gameMap.getCell(1, 1).getActor());
                    assertEquals(player, gameMap.getCell(2, 1).getActor());
                }

                @DisplayName("stairs down")
                @Test
                void stairsDownPlayer() {
                    GameMap nextMap = new GameMap(3, 3, CellType.FLOOR);
                    Player player = new Player(gameMap.getCell(1, 1));
                    gameMap.setNextMap(nextMap);
                    gameMap.setPlayer(player);
                    nextMap.setPlayer(player);
                    gameMap.getCell(2, 1).setType(CellType.STAIRS_DOWN);
                    if (player.validateMove(1, 0)) {
                        player.move(1, 0);
                    }
                    assertEquals(2, player.getX());
                    assertEquals(1, player.getY());
                    assertNull(gameMap.getCell(1, 1).getActor());
                    assertEquals(player, gameMap.getCell(2, 1).getActor());
                }
            }

            @Nested
            @DisplayName("actor is not player")
            class StairsNotPlayerTest {
                @DisplayName("stairs up")
                @Test
                void stairsUpNotPlayer() {
                    gameMap.getCell(2, 1).setType(CellType.STAIRS_UP);
                    Skeleton skeleton = new Skeleton(gameMap.getCell(1, 1));
                    if (skeleton.validateMove(1, 0)) {
                        skeleton.move(1, 0);
                    }
                    assertEquals(1, skeleton.getX());
                    assertEquals(1, skeleton.getY());
                    assertNull(gameMap.getCell(2, 1).getActor());
                    assertEquals(skeleton, gameMap.getCell(1, 1).getActor());
                }

                @DisplayName("stairs down")
                @Test
                void stairsDownNotPlayer() {
                    gameMap.getCell(2, 1).setType(CellType.STAIRS_DOWN);
                    Skeleton skeleton = new Skeleton(gameMap.getCell(1, 1));
                    if (skeleton.validateMove(1, 0)) {
                        skeleton.move(1, 0);
                    }
                    assertEquals(1, skeleton.getX());
                    assertEquals(1, skeleton.getY());
                    assertNull(gameMap.getCell(2, 1).getActor());
                    assertEquals(skeleton, gameMap.getCell(1, 1).getActor());
                }
            }
        }

        @Nested
        @DisplayName("wall when")
        class WallTest {
            @DisplayName("noClip is off")
            @Test
            void cannotMoveIntoWall() {
                gameMap.getCell(2, 1).setType(CellType.WALL);
                Player player = new Player(gameMap.getCell(1, 1));
                if (player.validateMove(1, 0)) {
                    player.move(1, 0);
                }
                assertEquals(1, player.getX());
                assertEquals(1, player.getY());
                assertNull(gameMap.getCell(2, 1).getActor());
                assertEquals(player, gameMap.getCell(1, 1).getActor());
            }

            @DisplayName("noClip is on")
            @Test
            void canMoveIntoWall() {
                gameMap.getCell(2, 1).setType(CellType.WALL);
                Player player = new Player(gameMap.getCell(1, 1));
                player.setCheater();
                if (player.validateMove(1, 0)) {
                    player.move(1, 0);
                }
                assertEquals(2, player.getX());
                assertEquals(1, player.getY());
                assertNull(gameMap.getCell(1, 1).getActor());
                assertEquals(player, gameMap.getCell(2, 1).getActor());
            }
        }


        // player move except wall with noClip and without it, enemy move

        @DisplayName("out of index")
        @Test
        void cannotMoveOutOfMap() {
            Player player = new Player(gameMap.getCell(2, 1));
            if (player.validateMove(1, 0)) {
                player.move(1, 0);
            }
            assertEquals(2, player.getX());
            assertEquals(1, player.getY());
            assertEquals(player, gameMap.getCell(2, 1).getActor());
        }

        @Nested
        @DisplayName("actor when")
        class ActorMoveTest {
            @DisplayName("noClip is off")
            @Test
            void cannotMoveIntoAnotherActor() {
                Player player = new Player(gameMap.getCell(1, 1));
                Skeleton skeleton = new Skeleton(gameMap.getCell(2, 1));
                if (player.validateMove(1, 0)) {
                    player.move(1, 0);
                }
                assertEquals(1, player.getX());
                assertEquals(1, player.getY());
                assertEquals(2, skeleton.getX());
                assertEquals(1, skeleton.getY());
                assertEquals(skeleton, gameMap.getCell(2, 1).getActor());
                assertEquals(player, gameMap.getCell(1, 1).getActor());
            }

            @DisplayName("noClip is on")
            @Test
            void canMoveIntoAnotherActor() {
                Player player = new Player(gameMap.getCell(1, 1));
                Skeleton skeleton = new Skeleton(gameMap.getCell(2, 1));
                player.setCheater();
                if (player.validateMove(1, 0)) {
                    player.move(1, 0);
                }
                assertEquals(2, player.getX());
                assertEquals(1, player.getY());
                assertEquals(2, skeleton.getX());
                assertEquals(1, skeleton.getY());
                assertEquals(player, gameMap.getCell(2, 1).getActor());
                assertNull(gameMap.getCell(1, 1).getActor());
            }
        }
    }

    @Nested
    @DisplayName("healUp when")
    class HealUpTest {

        @DisplayName("health is positive")
        @Test
        void healsUp() {
            Player player = new Player(gameMap.getCell(1, 1));
            player.setHealth(10);
            player.healUp(5);
            assertEquals(15, player.getHealth());
        }

        @DisplayName("health is negative")
        @Test
        void doesNotHealUp() {
            Player player = new Player(gameMap.getCell(1, 1));
            player.setHealth(10);
            player.healUp(-5);
            assertEquals(10, player.getHealth());
        }
    }

    @Nested
    @DisplayName("sufferDamage when")
    class SufferDamageTest {
        @DisplayName("damage is positive")
        @Test
        void suffersDamage() {
            Skeleton skeleton = new Skeleton(gameMap.getCell(1, 1));
            skeleton.setHealth(10);
            skeleton.sufferDamage(5);
            assertEquals(5, skeleton.getHealth());
        }

        @DisplayName("damage is negative")
        @Test
        void doesNotSufferDamage() {
            Skeleton skeleton = new Skeleton(gameMap.getCell(1, 1));
            skeleton.setHealth(10);
            skeleton.sufferDamage(-5);
            assertEquals(10, skeleton.getHealth());
        }
    }

    @Nested
    @DisplayName("updateIsAlive when")
    class UpdateIsAliveTest {
        @DisplayName("actor is alive")
        @Test
        void aliveActor() {
            Skeleton skeleton = new Skeleton(gameMap.getCell(1, 1));
            skeleton.setHealth(10);
            skeleton.updateIsAlive();
            assertTrue(skeleton.isAlive());
        }

        @DisplayName("actor is dead")
        @Test
        void deadActor() {
            Skeleton skeleton = new Skeleton(gameMap.getCell(1, 1));
            skeleton.setHealth(-5);
            skeleton.updateIsAlive();
            assertFalse(skeleton.isAlive());
        }

    }
    @Nested
    @DisplayName("isNeighborCellType when")
    class IsNeighborCellTypeTest {

        @DisplayName("cellType is same")
        @Test
        void sameCellType() {
            Player player = new Player(gameMap.getCell(1, 1));
            gameMap.getCell(2, 1).setType(CellType.DOOR);
            assertTrue(player.isNeighborCellType(1, 0, CellType.DOOR));
        }

        @DisplayName("cellType is not the same")
        @Test
        void differentCellType() {
            Player player = new Player(gameMap.getCell(1, 1));
            gameMap.getCell(2, 1).setType(CellType.WALL);
            assertFalse(player.isNeighborCellType(1, 0, CellType.DOOR));
        }

        @DisplayName("dx is invalid")
        @Test
        void cellTypeDxIsInvalid() {
            Player player = new Player(gameMap.getCell(1, 1));
            assertFalse(player.isNeighborCellType(-5, 0, CellType.DOOR));
        }

        @DisplayName("dy is invalid")
        @Test
        void cellTypeDyIsInvalid() {
            Player player = new Player(gameMap.getCell(1, 1));
            assertFalse(player.isNeighborCellType(0, -5, CellType.DOOR));
        }
    }

    @Nested
    @DisplayName("isNeighborItem when")
    class IsNeighborItemTest {
        @DisplayName("neighbor has item")
        @Test
        void neighborHasItem() {
            Player player = new Player(gameMap.getCell(1, 1));
            gameMap.getCell(2, 1).setItem(new Coin(gameMap.getCell(2, 1)));
            assertTrue(player.isNeighborItem(1, 0));
        }

        @DisplayName("neighbor has no item")
        @Test
        void neighborHasNoItem() {
            Player player = new Player(gameMap.getCell(1, 1));
            assertFalse(player.isNeighborItem(1, 0));
        }

        @DisplayName("dx is invalid")
        @Test
        void neighborItemDxIsInvalid() {
            Player player = new Player(gameMap.getCell(1, 1));
            assertFalse(player.isNeighborItem(-5, 0));
        }

        @DisplayName("dy is invalid")
        @Test
        void neighborItemDyIsInvalid() {
            Player player = new Player(gameMap.getCell(1, 1));
            assertFalse(player.isNeighborItem(0, -5));
        }
    }

    @Nested
    @DisplayName("isNeighborActor when")
    class IsNeighborActorTest {
        @DisplayName("neighbor has actor")
        @Test
        void neighborHasActor() {
            Player player = new Player(gameMap.getCell(1, 1));
            gameMap.getCell(2, 1).setActor(new Skeleton(gameMap.getCell(2, 1)));
            assertTrue(player.isNeighborActor(1, 0));
        }

        @DisplayName("neighbor has no actor")
        @Test
        void neighborHasNoActor() {
            Player player = new Player(gameMap.getCell(1, 1));
            assertFalse(player.isNeighborActor(1, 0));
        }

        @DisplayName("dx is invalid")
        @Test
        void neighborActorDxIsInvalid() {
            Player player = new Player(gameMap.getCell(1, 1));
            assertFalse(player.isNeighborActor(-5, 0));
        }

        @DisplayName("dy is invalid")
        @Test
        void neighborActorDyIsInvalid() {
            Player player = new Player(gameMap.getCell(1, 1));
            assertFalse(player.isNeighborActor(0, -5));
        }
    }
}