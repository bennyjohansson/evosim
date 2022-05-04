
from turtle import pos
from unicodedata import name
import pandas as pd
import numpy as np
from random import sample, seed
from itertools import product
from creatureworld.Creature import Creature, types_of_creatures

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
        self._creatures_list = []

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
        self._grid.iloc[coordinate[0], coordinate[1]] = 'c'#creature

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


    def placeCreatures(self, creatures: list[Creature]):
        numCreatures = len(creatures)
        randomplaces = sample(list(product(range(self.w_dim[0]), range(self.w_dim[1]))), k=numCreatures)
        for i, cr in enumerate(creatures):
            self.placeCreature(randomplaces[i], cr)
            cr.position = randomplaces[i]
            self._creatures_list.append(cr)

    def iterateWorld(self):
        for n in range(len(self._creatures_list)):
            print("Creature make move: ", self._creatures_list[n])
            #isinstance( self._creatures_list[n], Creature)
            #print(isinstance( self._creatures_list[n], Creature))
            creaturesSurrounding = self.creatureSurroundings(self._creatures_list[n].position,1)
            #print("My suroundings: ")
            #print(creaturesSurrounding)
            creatureMove = self._creatures_list[n].make_move(creaturesSurrounding)
            print(creatureMove)

    def creatureSurroundings(self, position = (9,15), vision=1) -> pd.Series:
        '''
        Function that returns the suroundings of a creature
        Input: position of creature
        Input: vision - only =1 supported
        Returns: Normalised data for each surrounding block
        123
        8c4
        765
        Each block -1 column wall 0 column empty c creature F food
        1000 = Wall
        0100 = Empty
        0010 = Creature
        0001 = Food
        '''
        # Wall detection
        wall = -1
        # Behöver generalisera för att funka med vision som inte är 1
        # övre/undre raden börjar på 3 vision ett och växer med +2 för varje vision
        # höger/vänster kolumn börjar på 1 vision ett, växer med +2 för varje vision
        # Onödigt komplext tror jag men fick öva på pandas...

        upper = False
        right = False
        bottom = False
        left = False

        # Detect walls
        if position[0] - 1 <= -1: # Upper wall
            upper = True
        if position[1] +1 >= self.w_dim[1]: # Right wall
            right = True
        if position[0] + 1 >= self.w_dim[0]: # Bottom wall
            bottom = True
        if position[1] - 1 <= -1: # Left wall
            left = True
        


        if upper and right: # Upper right corner
            #print("Upper right")
            ser1 = pd.Series([wall for i in range(3)])
            ser2 = pd.Series([wall])
            ser3 = pd.concat([pd.Series([wall]), pd.Series(self._grid.iloc[position[0]+1][position[1]:position[1]-1-1:-1])]) # Kan vara dumt då out of bounds indexering kan ge tom dataframe som jag märkte nedan
            ser4 = pd.Series(self._grid.iloc[position[0]][position[1]-1])  
        
        if bottom and right: # Bottom right corner
            #print("Bottom right")
            ser1 = pd.concat([pd.Series(self._grid.iloc[position[0]-1][position[1]-1:position[1]+1:1]), pd.Series([wall])]) # Kan vara dumt då out of bounds indexering kan ge tom dataframe som jag märkte nedan
            ser2 = pd.Series([wall])
            ser3 = pd.Series([wall for i in range(3)])
            ser4 = pd.Series(self._grid.iloc[position[0]][position[1]-1])  
        
        if upper and left: # Upper left corner
            #print("Upper left")
            ser1 = pd.Series([wall for i in range(3)])
            ser2 = pd.Series(self._grid.iloc[position[0]][position[1]+1])
            ser3 = pd.concat([pd.Series([self._grid.iloc[position[0]+1][position[1]+1], self._grid.iloc[position[0]+1][position[1]]]), pd.Series([wall])])
            ser4 = pd.Series([wall])
        
        if bottom and left: # Bottom left corner
            #print("Bottom left")
            ser1 = pd.concat([pd.Series([wall]),pd.Series([self._grid.iloc[position[0]-1][position[1]], self._grid.iloc[position[0]-1][position[1]+1]])])
            ser2 = pd.Series(self._grid.iloc[position[0]][position[1]+1])
            ser3 = pd.Series([wall for i in range(3)])
            ser4 = pd.Series([wall])

        # Detect right wall column+1 out-of-bounds
        if right and not (upper or bottom):
            #print("right but not")
            ser1 = pd.Series([self._grid.iloc[position[0]-1][position[1]-1], \
                            self._grid.iloc[position[0]-1][position[1]], \
                             wall])
            ser2 = pd.Series([wall])
            ser3 = pd.Series([wall, \
                        self._grid.iloc[position[0]+1][position[1]], \
                         self._grid.iloc[position[0]+1][position[1]-1]])
            ser4 = pd.Series([self._grid.iloc[position[0]][position[1]-1]])
            
        # Detect left wall column-1 out-of-bounds
        if left  and not (upper or bottom):
            #print("Left but not")
            ser1 = pd.Series([wall, \
                        self._grid.iloc[position[0]-1][position[1]], \
                         self._grid.iloc[position[0]-1][position[1]+1]])
            ser2 = pd.Series(self._grid.iloc[position[0]][position[1]+1])
            ser3 = pd.Series([self._grid.iloc[position[0]+1][position[1]+1], \
                            self._grid.iloc[position[0]+1][position[1]],\
                            wall])
            ser4 = pd.Series([wall])

        
        # Detect lower wall row+1 out-of-bounds
        if bottom and not (left or right):
            #print("Hit the bottom")
            ser1 = pd.Series(self._grid.iloc[position[0]-1][position[1]-1:position[1]+1+1])
            ser2 = pd.Series(self._grid.iloc[position[0]][position[1]+1])
            ser3 = pd.Series([wall for i in range(3)])
            ser4 = pd.Series(self._grid.iloc[position[0]][position[1]-1])  


        # Detect upper wall row-1 out-of-bounds
        if upper and not (left or right):
            #print("Hit the ceiling")
            ser1 = pd.Series([wall for i in range(3)])
            ser2 = pd.Series(self._grid.iloc[position[0]][position[1]+1])
            ser3 = pd.Series([self._grid.iloc[position[0]+1][position[1]+1], \
                            self._grid.iloc[position[0]+1][position[1]],\
                            self._grid.iloc[position[0]+1][position[1]-1]])
            ser4 = pd.Series(self._grid.iloc[position[0]][position[1]-1])  
                 

        if not (upper or bottom or right or left):
            #print("not a corner")
            ser1 = pd.Series(self._grid.iloc[position[0]-1][position[1]-1:position[1]+1+1])
            ser2 = pd.Series(self._grid.iloc[position[0]][position[1]+1])
            ser3 = pd.Series([self._grid.iloc[position[0]+1][position[1]+1], \
                            self._grid.iloc[position[0]+1][position[1]],\
                            self._grid.iloc[position[0]+1][position[1]-1]])
            ser4 = pd.Series(self._grid.iloc[position[0]][position[1]-1])  


        surroundings= pd.concat([ser1,ser2,ser3,ser4])
        dummies = pd.get_dummies(surroundings) # drop_first funkar inte i det här läget
        print(position)
        # print(surroundings)
        # print(dummies)
        # print(dummies.columns)
        if len(dummies.columns) <4:
            # print("need to normalise categorical data vision")
            all = pd.Series([-1,0,'c','F'])
            diff = all[all.isin(dummies.columns) == False]
            for c in diff:
                # print("adding missing column: ", c)
                # Create named series
                new_col = pd.Series(np.zeros(8, dtype=int), name=c)
                # print(new_col)
                dummies[new_col.name] = new_col.values

                # print(dummies)
        dummies = dummies[[-1,0,'c','F']] # Normalise order wall, empty, creature, food
        # print(dummies)

        return dummies
        #print("Ser1: \n ",ser1, "ser2: \n ",ser2,"ser3: \n", ser3, "ser4: \n", ser4)



def main():
    
    my_world = World((17,17))
    # print(my_world)
    print("Dimension of world: {}".format(my_world.w_dim))
    #my_world.placeCreature((4,5), object())
    #print(my_world)

    my_world.createFoodSupply(0.3)
    print(my_world)

    creature_list = [Creature(types_of_creatures[0]), Creature(types_of_creatures[1])]

    my_world.placeCreatures(creature_list)
    print(my_world)

    #my_world.iterateWorld()

    my_world.creatureSurroundings()


if __name__ == "__main__":
    main()