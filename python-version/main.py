from creatures.Creature import Creature, types_of_creatures
from world.World import World

def main():
    worldDimensions = (7,7)
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

    # Place creatures randomly in world
    # theWorld.placeCreatures(myCreatures)

    # Iterate the world
    # While world.condition iterate
    # Creatures make a choice in each iteration by AI algo
    # Creature.analyse_and_make_move --> move, eat, fight
    # World end condition

    

if __name__ == "__main__":
    main()


