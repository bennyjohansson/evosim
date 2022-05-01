from creatureworld.Creature import Creature, types_of_creatures
from creatureworld.World import World

def main():
    worldDimensions = (17,17)
    #numberOfCreatures = 10

    theWorld = World(worldDimensions)
    print(theWorld)
    
    # Initiate food / and other loot in world
    theWorld.createFoodSupply(0.3)
    print(theWorld)

    # Create creatures with random types, strength, energy
    mycreatures = [Creature(types_of_creatures[0]), Creature(types_of_creatures[1]) ]
    print(mycreatures)

    theWorld.placeCreatures(mycreatures)

    print(theWorld)

    # Iterate the world
    # While world.condition iterate
    theWorld.iterateWorld()
    # Creatures make a choice in each iteration by AI algo
    # Creature.analyse_and_make_move --> move, eat, fight
    # World end condition

    

if __name__ == "__main__":
    main()


