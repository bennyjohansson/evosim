import time
import pandas as pd
import numpy as np

types_of_creatures = [
    {'type': 'hitman','aggressivness': 1, 'hunger':0, 'vision': 2},    # Always attack
    {'type': 'fatman','aggressivness': 0, 'hunger':1, 'vision': 1},     # Always go for food
    {'type': 'runman','aggressivness': 0, 'hunger':0, 'vision': 1}      # Run away, if opponent nearby otherwise go for food
]


class Creature:
    """ The creature that can exist in the world """
    # def __init__(self) -> None:
    #     self._c_id = 0    
    
    def __init__(self, c_type: map, ) -> None:
        """ Defines a creature takes id, type, world, strength, energy"""
        print("Init creature")
        self._c_id = time.time_ns() 
        self._c_type  = c_type
        self._position = ()

    # Define getter / setter methods
    def get_c_id(self):
        return self._c_id

    def set_c_id(self,val):
        self._c_id = val

    c_id = property(get_c_id, set_c_id)

    def get_position(self):
        return self._position

    def set_position(self,val):
        self._position = val

    position = property(get_position, set_position)

    # String representation of creature
    def __str__(self) -> str:
        #return "My id is {} and I am a {} my vision is {}".format(self._c_id, self._c_type, self.vision())
        return "id:{}, pos: {}".format(self._c_id, self.position)

    def vision(self):
        return self._c_type['vision']

    def make_move(self, surroundings: pd.array):
        # Call brain with surroundings and make move
        # Normalise order wall, empty, creature, food
        # Attack
        # Eat
        # Move
        # Do nothing
        print("Iam: ", self)
        return surroundings.to_numpy().flatten()

def main():
    # Test classes
    mycreature =  Creature(types_of_creatures[0])
    # String rep
    print(mycreature)
    print(mycreature.make_move(pd.array(np.arange(5))))


if __name__ == "__main__":
    main()