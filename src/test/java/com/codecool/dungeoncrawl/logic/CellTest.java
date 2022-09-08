package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Skeleton;
import com.codecool.dungeoncrawl.logic.items.Coin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cell")
class CellTest {
    GameMap map = new GameMap(3, 3, CellType.FLOOR);
    Coin coin;
    Skeleton skeleton;

    @BeforeEach
    void setup() {
        Cell cell1 = map.getCell(1, 1);
        Cell cell2 = map.getCell(1, 0);
        cell1.setItem(new Coin(cell1));
        coin = (Coin) cell1.getItem();
        cell2.setActor(new Skeleton(cell2));
        skeleton = (Skeleton) cell2.getActor();
    }

    @DisplayName("getNeighbor when")
    @Nested
    class GetNeighborTest {

        @DisplayName("both coordinates are valid, and cell has neighbor")
        @Test
        void getNeighbor() {
            Cell cell = map.getCell(1, 1);
            Cell neighbor = cell.getNeighbor(-1, 0);
            assertEquals(0, neighbor.getX());
            assertEquals(1, neighbor.getY());
        }

        @DisplayName("both coordinates are valid, and cell has no neighbor")
        @Test
        void cellOnEdgeHasNoNeighbor() {
            Cell cell = map.getCell(1, 0);
            assertEquals(null, cell.getNeighbor(0, -1));

            cell = map.getCell(1, 2);
            assertEquals(null, cell.getNeighbor(0, 1));
        }

        @DisplayName("dx is invalid")
        @Test
        void dxIsInvalid() {
            Cell cell = map.getCell(1, 0);
            assertEquals(null, cell.getNeighbor(-5, 0));
        }

        @DisplayName("dy is invalid")
        @Test
        void dyIsInvalid() {
            Cell cell = map.getCell(1, 0);
            assertEquals(null, cell.getNeighbor(0, -6));
        }
    }

    @DisplayName("getNeighborItem when")
    @Nested
    class GetNeighborItemTest {

        @DisplayName("both coordinates are valid, and neighbor has item")
        @Test
        void NeighborHasItem() {
            Cell cell = map.getCell(0, 0);
            assertEquals(coin, cell.getNeighborItem(1, 1));
        }

        @DisplayName("both coordinates are valid, and neighbor has no item")
        @Test
        void NeighborHasNoItem() {
            Cell cell = map.getCell(1, 1);
            assertEquals(null, cell.getNeighborItem(1, 1));
        }

        @DisplayName("dx is invalid")
        @Test
        void itemDxIsInvalid() {
            Cell cell = map.getCell(0, 0);
            assertEquals(null, cell.getNeighborItem(-5, 1));
        }

        @DisplayName("dy is invalid")
        @Test
        void itemDyIsInvalid() {
            Cell cell = map.getCell(0, 0);
            assertEquals(null, cell.getNeighborItem(0, -5));
        }
    }

    @DisplayName("getNeighborActor when")
    @Nested
    class GetNeighborActorTest {

        @DisplayName("both coordinates are valid, and neighbor has actor")
        @Test
        void NeighborHasActor() {
            Cell cell = map.getCell(0, 0);
            assertEquals(skeleton, cell.getNeighborActor(1, 0));
        }

        @DisplayName("both coordinates are valid, and neighbor has no actor")
        @Test
        void NeighborHasNoActor() {
            Cell cell = map.getCell(1, 1);
            assertEquals(null, cell.getNeighborActor(1, 1));
        }

        @DisplayName("dx is invalid")
        @Test
        void itemDxIsInvalid() {
            Cell cell = map.getCell(0, 0);
            assertEquals(null, cell.getNeighborActor(-5, 1));
        }

        @DisplayName("dy is invalid")
        @Test
        void itemDyIsInvalid() {
            Cell cell = map.getCell(0, 0);
            assertEquals(null, cell.getNeighborActor(0, -5));
        }
    }
}