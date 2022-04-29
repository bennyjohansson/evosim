
import pandas as pd
import numpy as np
from random import sample, seed
from itertools import product
import sys
sys.path.append("/Users/erikedin/Utveckling/Bennyprojekt/evosim")
from creatures.Creature import Creature

def initialise_frame(w_dim: tuple):
    df = pd.DataFrame(index=range(w_dim[0]), columns=range(w_dim[1]))
    df = df.fillna(0)
    return df

class World:
    """
    Implements a grid world to place food and creatures in.
    """
    def __init__(self, w_dim: tuple) -> None:
        self._w_dim = w_dim
        self._grid = initialise_frame(w_dim) #pd.DataFrame(index=range(w_dim[0]), columns=range(w_dim[1]))
        self._creatures_list = [Creature]

    def __str__(self) -> str:
        return "I am the world of dimension: {}\n matrix: \n {} \n with area: {} \n creatures list {}".\
            format(self._w_dim, self._grid, self.area(), self._creatures_list)

    def get_w_dim(self):
        return self._w_dim

    w_dim = property(get_w_dim)

    def area(self) -> int:
        """Returns the area of the world"""
        return self._w_dim[0] * self._w_dim[1]

    def placeFood(self, coordinate):
        self._grid.iloc[coordinate[0], coordinate[1]] = 'F'

    def placeCreature(self, coordinate, creature):
        self._grid.iloc[coordinate[0], coordinate[1]] = creature

    def createFoodSupply(self, percentFood: float) -> None:
        """ Fills the world randomly with food. 0 --> no food, 1 --> food on each part """
        #seed(42)
        availablePositions = self.area()
        foodToBePlaced = int(availablePositions * percentFood)
        print("will randomly place {} food items".format(foodToBePlaced))
        randomplaces = sample(list(product(range(self.w_dim[0]), range(self.w_dim[1]))), k=foodToBePlaced)
        print("Places to place food {}".format(randomplaces))
        for place in randomplaces:
            self.placeFood(place)


    def placeCreatures(self, creatures: list):
        numCreatures = len(creatures)
        randomplaces = sample(list(product(range(self.w_dim[0]), range(self.w_dim[1]))), k=numCreatures)
        for i, creature in enumerate(creatures):
            self.placeCreature(randomplaces[i], creature)
            creature.position = randomplaces[i]
            self._creatures_list.append(creature)

    def iterateWorld(self):
        for c in self._creatures_list:
            print(c.make_move(pd.array(np.arange(5))))



def main():
    
    my_world = World((10,7))
    # print(my_world)
    print("Dimension of world: {}".format(my_world.w_dim))
    my_world.placeCreature((4,5), object())
    print(my_world)

    my_world.createFoodSupply(0.3)
    print(my_world)


if __name__ == "__main__":
    main()