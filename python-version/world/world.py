import pandas as pd
from random import sample, seed
from itertools import product

class World:
    """
    Implements a grid world to place food and creatures in.
    """
    def __init__(self, w_dim: tuple) -> None:
        self._w_dim = w_dim
        self.grid = pd.DataFrame(index=range(w_dim[0]), columns=range(w_dim[1]))

    def __str__(self) -> str:
        return "I am the world of dimension: {}\n matrix: \n {} \n with area: {}".\
            format(self._w_dim, self.grid, self.area())

    def get_w_dim(self):
        return self._w_dim

    w_dim = property(get_w_dim)

    def area(self) -> int:
        """Returns the area of the world"""
        return self._w_dim[0] * self._w_dim[1]

    def placeFood(self, coordinate):
        self.grid.iloc[coordinate[0], coordinate[1]] = 'F'


    def createFoodSupply(self, percentFood: float) -> None:
        seed(42)
        availablePositions = self.area()
        foodToBePlaced = int(availablePositions * percentFood)
        print("will randomly place {} food items".format(foodToBePlaced))
        randomplaces = sample(list(product(range(self.w_dim[0]), range(self.w_dim[1]))), k=foodToBePlaced)
        print("Places to place food {}".format(randomplaces))
        for place in randomplaces:
            self.placeFood(place)






def main():
    my_world = World((10,7))
    # print(my_world)
    print("Dimension of world: {}".format(my_world.w_dim))
    my_world.grid.iloc[1,1] = 'C'
    print(my_world)

    my_world.createFoodSupply(0.40)
    print(my_world)


if __name__ == "__main__":
    main()