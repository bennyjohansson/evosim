import time
import pandas as pd

types_of_creatures = [
    {'type': 'hitman','aggressivness': 1, 'hunger':0, 'vision': 2},    # Always attack
    {'type': 'fatman','aggressivness': 0, 'hunger':1, 'vision': 1},     # Always go for food
    {'type': 'runman','aggressivness': 0, 'hunger':0, 'vision': 1}      # Run away, if opponent nearby otherwise go for food
]


class Creature:
    """ The creature that can exist in the world """
    def __init__(self) -> None:
        self._c_id = 0    
    
    def __init__(self, c_type: map, ) -> None:
        """ Defines a creature takes id, type, world, strength, energy"""
        self._c_id = time.time_ns() 
        self._c_type  = c_type

    # Define getter / setter methods
    def get_c_id(self):
        return self._c_id

    def set_c_id(self,val):
        self._c_id = val

    c_id = property(get_c_id, set_c_id)

    # String representation of creature
    def __str__(self) -> str:
        return "My id is {} and I am a {} my vision is {}".format(self._c_id, self._c_type, self.vision())

    def vision(self):
        return self._c_type['vision']

    def make_move(surroundings: pd.array):
        # Call brain with surroundings and make move
        pass

def main():
    # Test classes
    mycreature =  Creature(types_of_creatures[0])
    # String rep
    print(mycreature)


if __name__ == "__main__":
    main()