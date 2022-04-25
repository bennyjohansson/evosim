class Creature:
    """ The creature that can exist in the world """
    def __init__(self) -> None:
        self._c_id = 0    
    
    def __init__(self,  c_id: int,  c_type: int,  c_world: int,  c_strength: int,  c_energy: int) -> None:
        """ Defines a creature takes id, type, world, strength, energy"""
        self._c_id = c_id
        self._c_type  = c_type
        self._c_world = c_world
        self._c_strength = c_strength
        self._c_energy = c_energy

    # Define getter / setter methods
    def get_c_id(self):
        return self._c_id

    def set_c_id(self,val):
        self._c_id = val

    c_id = property(get_c_id, set_c_id)

    # String representation of creature
    def __str__(self) -> str:
        return "My id {} my type {} my world {} my strength {} my energy {}".format(self._c_id, self._c_type, self._c_world, self._c_strength, self._c_energy)


def main():
    # Test class
    mycreature =  Creature(1,2,3,4,5)

    # String rep
    print(mycreature)

    # change property
    mycreature.c_id=2
    print(mycreature)

if __name__ == "__main__":
    main()