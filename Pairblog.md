Comp2511 Assignment II group project  - Rishi Adhvaryu, Taha Hussain
Assignment II Pair Blog

Task 1) Code Analysis and Refactoring ⛏️
a) From DRY to Design Patterns

Links to your merge requests
https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/M11C_ABRA/assignment-ii/-/merge_requests/2/diffs

i. Look inside src/main/java/dungeonmania/entities/enemies. Where can you notice an instance of repeated code? Note down the particular offending lines/methods/fields.
The code in the move method for mercenary and zombietoast have repeated cases for invisibility and invincibility potions.

ii. What Design Pattern could be used to improve the quality of the code and avoid repetition? Justify your choice by relating the scenario to the key characteristics of your chosen Design Pattern.
To fix this, we can use a Strategy to execute enemy movement based on the context of  a move. The Strategy pattern allows us to create a series of different methods that can be swapped around and used when necessary. In this case, we would create strategies for move with invisible player and move with invincible player. We would create a moveStrategy interface in the enemies folder and give the enemies an instance of this interface that allows them to tell which movement strategy should be used and when. Then, when a move is called, we can update the moveStrategy to check which strategy needs to be used and use it accordingly. 

iii. Using your chosen Design Pattern, refactor the code to remove the repetition.
	I created a Strategy pattern for all the various different types of movement performed 	by the enemy entities. I then changed the Enemy class to contain a strategy variable 	in it that could be swapped out depending on the situation and type of enemy. Thus, 	enemies could use the same movement patterns as well as different ones, improving 	the versatility of the objects as well as reducing code repetition.


b) Observer Pattern
Identify one place where the Observer Pattern is present in the codebase, and outline how the implementation relates to the key characteristics of the Observer Pattern.
The Game class observes specific Enemies like zombieSpawner and zombie to track and notify their actions. This is evident from how the Game class ‘registers’ objects with their string id and also unsubscribes them when they are to be destroyed. This makes the Game class the subject of the Observer pattern and the objects, ie zombieSpawner, zombies, Spiders, the listeners, getting notified by every run tick from the game and using their methods to move and act accordingly. The observer pattern, hence allows the game to run, as it enables communication between the game and enemies, to inform their movements. 


c) Inheritance Design
Links to your merge requests
i. Name the code smell present in the above code. Identify all subclasses of Entity which have similar code smells that point towards the same root cause.

Merge requests	: https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/M11C_ABRA/assignment-ii/-/merge_requests/1

The code smell present in the above code is "Refused Bequest". This smell occurs when a subclass only uses a small portion of its superclass's methods and properties, indicating that the subclass and superclass may have an inappropriate relationship. 
In the case of the Exit class it inherits the onOverlap(), onMovedAway(), onDestroy() abstract methods from the Entity superclass, but it does not provide any functionality for these methods as they are essentially empty methods which is a sign of the “Refused Bequest” smell. The LSP is being violated because the Entity class is forcing certain behaviours on all subclasses. Other affected classes are: 
Buildabale.java, Potion.java, Arrow.java, Bomb.java, Key.java, Sword.java, Treasure.java, Wood.java, ZombieToastSpawner.java, Boulder.java, Door.java, Exit.java, Player.java, Portal.java, Switch.java and Wall.java

ii. Redesign the inheritance structure to solve the problem, in doing so remove the smells.
To fix this smell it was first noted that different classes were implementing different combinations of the abstract methods, as shown below:

None: Buildable, Exit, Wall
onOverlap(): Collectables, Boulder, Door, Player, Portal.
onOverlap(), OnDestroy: Enemy
onOverlap(), onMovedAway(): Switch
onDestroy(): ZombieToastSpawner
onOverlap(), onDestroy(), onMovedAway(): None

So to fix the problem three new interfaces were created: “Overlapable”, “Destroyable” and “MovedAwayListener”, each containing the respective method. Classes that needed to utilise the method would implement the appropriate interface and override the method.


d) More Code Smells

https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/M11C_ABRA/assignment-ii/-/merge_requests/6

i. What design smell is present in the above description?
The code smell in the description is that the code has high coupling and low cohesion. Elements in the code have been intrinsically coupled with one-another, and now trying to change one class or method has effects and requirements all across the code in what should be unrelated locations. This has also led to much repeated code in the entity subclasses, where many methods have been left empty and unused. 

ii. Refactor the code to resolve the smell and underlying problem causing it.
As a solution to this, I made an abstract subclass to entity called collectable that separates the entity class and allows for collectables to be altered later without having to change all the collectables individually, or have to directly alter the entity superclass.


e) Open-Closed Goals
https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/M11C_ABRA/assignment-ii/-/merge_requests/6
i. Do you think the design is of good quality here? Do you think it complies with the open-closed principle? Do you think the design should be changed?

The design of Goals does not fully comply with the Open-Closed Principle (OCP). The OCP states that software entities should be open for extension but closed for modification. 
Goal and GoalFactory do not adhere to this as:
1. In the Goal class the achieved() method uses a switch statement to decide the behaviour based on the type field. This means that if a new goal was added the achieved() method would need to be modified to add another case which violates the OCP.
2. In GoalFactory the createGoal() method uses a switch statement to create different types of Goal objects based off of a string argument. If a new goal type was added then this method would need to be modified again violating the OCP. 

ii. If you think the design is sufficient as it is, justify your decision. If you think the answer is no, pick a suitable Design Pattern that would improve the quality of the code and refactor the code accordingly.
The original design did not adhere to the OCP as if a new goal type needs to be added then the original Goal and GoalFactory class need to be modified. This does not adhere to the OCP which states that objects should be open for extension but closed for modification.
In this case the composite pattern is the most suitable for improving the design.

The goal class was changed to an abstract class and was given two abstract methods: toString and achieved. This means that any class that extends Goal must implement these methods.
For each type of goal in the original code (“exit”, “boulder”, “treasure”, “AND”, “OR”), a separate class was created that extends Goal.
The GoalFactory class was also modified to create instances of these new classes instead of creating a new instance with a type string. 


f) Open Refactoring

Merge Request 1 - Player
https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/M11C_ABRA/assignment-ii/-/merge_requests/7

Violation of the Law of Demeter:
The original Player class had a state field that directly changed the state of the Player object by creating new state objects. This is a violation of the Law of Demeter, which states that an object should only communicate with its immediate neighbours and should not command another object's state. 
The code was refactored so that all changes to the state are made within Player using methods becomeNormal(), becomeInvisible(), becomeInvincible(). Additionally different states were represented using booleans instead of entire classes.

Misuse of the State Pattern:
The original PlayerState class and its subclasses had transitionBase(), transitionInvincible() and transitionInvisible() methods that were used to transition to other states. This is a misuse of the State Pattern because it creates unnecessary dependencies between the states and makes the code harder to maintain. 
After refactoring, the state pattern was removed and replaced with two booleans added to the Player class. The two booleans ‘isInvisible’ and ‘isInvincible’ determine the current state of the player and are updated and accessed using the methods becomeInvisible(), become Invincible(), becomeNormal(), isInvincible(), isInvisible() and isNormal().

Improving Code Readability and Maintainability:
The original Player class had a triggeredNext() method that used an if-else statement to determine which statement to transition to based on the type of potion. This makes the code harder to read and maintain if more potion types and corresponding states are added in the future. 
After refactoring, the if-else statement was replaced with becomeNormal(), becomeInvincible() and becomeInvisible() methods, which improve code readability thanks to clear method calls, making it easier to understand how the Player object changes. Also adding new potion effects on the player is easier, as only the Player class needs to be changed.

Merge Request 2 - Battles
https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/M11C_ABRA/assignment-ii/-/merge_requests/8
Improving Code Readability and Maintainability:
The original BattleStatistics class had 2 constructors that were identical to each other, with the only difference being that there are two fields that are set to the default value in one of the constructors. This makes the code more difficult and jarring to read as both constructors are quite long.
The code was refactored so that the first constructor called the second, improving readability as we only have to read 1 constructor instead of 2.
The original BattleFacade class had a battle(Game game, Player player, Enemy enemy) method that performs a series of tasks that is difficult to understand as it implements alot of functionality into a single method.
After refactoring, sections of battle(Game game, Player player, Enemy enemy) method were split up to improve readability. Two new methods called applyBuffs() and updateStatistics() were created. This change reduced the amount of code within the battle method by splitting it up, improving readability.

Merge Request 3 - Buildables
https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/M11C_ABRA/assignment-ii/-/merge_requests/9
Improving Code Readability and Maintainability:
The original Buildable abstract class had subclasses such as Bow and Shield. Both contained use(Game game) and getDurabiltiy() method with the same implementations. 
To remove this duplicated code these methods were moved into the Buildable abstract class alongside the durability field.
The original getBuildables() in Inventory.java has various fields that count the amount of instances that occur within inventory. The code is concise and readable however adding more buildables maintaining it may become difficult. Similarly in checkBuildCriteria(Player p, boolean remove, boolean forcefield, EntityFactory factory) in Inventory.java, by simply looking at the methods parameters a major flaw can be seen, it’s relying on boolean forcefield to determine what to build. This makes it very difficult to extend this method.

To fix these smells a strategy pattern was implemented (BuildStrategy) that will contain the required fields to check to see if the player can build the buildable as well as build it. To fix the issue with the boolean forcefield it was changed to String entity, that will tell us what the player wants to build. The code changes occurred in Player and Inventory. In player the build method was adjusted accordingly to give a String entity. In inventory a simple switch statement was made that replaced all the logic of checkBuildCriteria method, making it more readable.  Now adding a new buildable becomes much more easy, all that is needed is the creation of a BuildStrategy with its respective information, hence improving readability, extendabiltiy and maintainability altogether.


Task 2) Evolution of Requirements 👽

a) Microevolution - Enemy Goal
Working branch-
https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/M11C_ABRA/assignment-ii/-/merge_requests/10
Final merge branch-
https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/M11C_ABRA/assignment-ii/-/merge_requests/11

Assumptions
Whether allies destroyed by a player-placed bomb count towards the enemy goal is undefined.
Assuming that it is or will be possible for x number of enemies to spawn in the game when a goal to kill x enemies is created.

Design
Create “enemy_goal” class with achieved and toString methods that extend goal.java. 
Create a enemy goal type case in goal Factory.java
Create an enemies killed counter (private int enemiesKilled)  in Player.java along with a getter that can be called into the goal.java class. 
Use the getEntities method in gameMap.java to keep track of the spawners remaining on the map. 
Changes after review
Added an IsSpawnerActive boolean to spawners since they weren’t destroyed upon interaction with weapons. 
Test list
TestWorkingEnemy goal. Creates an enemy goal and completes it.
Test CompoundEnemy goal. Creates Compounded enemy goal with exit goal and completes it out of order, then in order.
TestCompoundEnemy goal 2. Creates an “or” enemy goal wit Treasure and checks it works


Choice 1 Snake
Working branch-
https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/M11C_ABRA/assignment-ii/-/merge_requests/13
Final merge branch-
https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/M11C_ABRA/assignment-ii/-/merge_requests/15
Snake invisibility patch branch - (fixes SnakeHead.canMoveTo, to work closer to spec) 
https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/M11C_ABRA/assignment-ii/-/merge_requests/20
Assumptions
Whether destroying a snake body counts towards the enemy goal is undefined
Whether a snake is affected by a swamp tile is undefined
Whether snakes consume sunstones is undefined.
Whether other snakes can move onto an invisible snake is undefined.

Design
Create a new class extending from Enemy.java called SnakeHead.java. SnakeHead.java has the following attributes.
-public static final double TREASURE_HEALTH_BONUS
-public static final double KEY_HEALTH_MULTIPLIER
-public static final double ARROW_ATTACK_MULTIPLIER
- double snake_attack
-double snake_health
-boolean invisibilityPotion
-boolean invincibilityPotion
-private Enemy child
In this class we would also have a movement method and an overlap method that takes collectibles and applies them to the snake accordingly. There would also be a SnakeBodyCreator method that creates snake body objects when the snake grows. 
We would also create a SnakeBody.java class that extends from snakeHead.java. This object would have the attributes
-private Enemy parent
The body would also have methods to update it according to what happens to the head as well as a movement method. The body would also have to convert it into a head if its parent is killed and it has the invincibility buff or delete all of its children if it doesn’t.
The Design also would involve the creation of two new movement strategies for the SnakeHead and SnakeBody. SnakeHead moves towards the nearest item, while SnakeBody moves to the previous location of its parent.

Changes after review
I had to add a get shortest distance method to gameMap that used the dijkstra pattern to find the nearest treasure. I also added a shortest manhattanDistance method in the Invisible Snake move strategy. I had to do a lot of cleaning up on my tests but I also improved the functionality of the tests while doing so, testing battles as well as movement better. 
I also realised that I would have to create a snakeCreate method in the entity factory to allow for snakes to be initialised on the map from config. This also meant I needed to make a specific constructor that takes in the item buffs and default health and attack doubles from the config files.
I also needed to create a variety of helper functions in my code, such as one that looked through SnakeHeads children and pulled out a sublist. 
Test list
TestSnakeMovement: tests that the snake moves to the nearest treasure twice and stops when there is no more treasure.
TestSnakeBuffed: tests that the snake gets health and damage buffed when it uses certain items.
TestSnakeInvisibility: test that the snake can travel through walls and other snakes when it is invisible.
TestSnakeInvincibilty: test that a snake splits multiple times when cut in half.


Choice 2 (Logic Switches)
https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/M11C_ABRA/assignment-ii/-/merge_requests/12
Assumptions
1 Wire can be connected to 3 separate Logical Entities and power all three given the circuit is complete.
When creating a Logical Entity it is given that there will be a logical rule attached to it.
Lightbulb and Switchdoor are always off/closed when created.
Spiders can walk onto closed SwitchDoors.
The player is not allowed to stand on LightBulbs
Design
Created a new interface LogicalEntity in logicalEntity
 	- Contains a single method ‘notify(GameMap map)’
Created a new class LightBulb that implements LogicalEntity in the logicalEntity folder 
	- notify method calls the logical rule strategy to determine whether the LightBulb should turn on or off			
Created a new class SwitchDoor that implements LogicalEntity in the logicalEntity folder
	- notify method calls the logical rule strategy to determine whether it should open or close
Created a new class Wire in the logicalEntity folder, that does not implement LogicalEntity
	- Similarly to how bombs are subscribed to a switch in creation of a switch, so does cardinally adjacent wires. (GameMap and Switch were updated)
	- The wire is notified whenever the switch is turned activated or deactivated
	- The wire performs a Depth-First-Search(DFS) to find all LogicalEntities in the 
	circuit to be notified.
Created a new abstract class LogicalRule in LogicalEntity
	- Contains all common logic needed to determine whether a logicalEntity 
	should be powered.
Created 4 new classes that extend LogicalRule: AndRule, OrRule, XorRule, CoAndRule
	- The four classes use methods within LogicalRule to return true or false based on
	their unique behaviour
	- All the classes are the same except for CoAndRule due to its unique behaviour 
	differentiating it from the rest.
Created a new class LogicalBomb which extends Bomb and implements LogicalEntity. 
	- notify method calls the logical rule strategy to determine if the LogicalBomb should           
	explode
Test list
[Test List]
Tests were made for LightBulb and included:
	- And & Or LogicalRule test
	- Xor LogicalRule test
	- CoAnd LogicalRule test
	- Turning a LightBulb on with only a switch and no wires
Tests were made for SwitchDoor and included:
	- And & Or LogicalRule test
	- Xor LogicalRule test
	- CoAnd LogicalRule test
	- Walking through a open and closed SwitchDoor test
Additional tests were made in BombTest for LogicalBombs and include:
	- And & Or LogicalRule test
	- Xor LogicalRule test
	- CoAnd LogicalRule test



Task 3) Investigation Task ⁉️

Merge Request 1: https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/M11C_ABRA/assignment-ii/-/merge_requests/16
For this merge request, we fixed the functionality of spawners in the game by destroying them when a player interacts with them (with a weapon, from a cardinally adjacent location). Previously, the spawner wasn’t destroyed, which was against what was written in the spec. We also updated tests that used spawners to expect this new functionality.

Merge Request 2
https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/M11C_ABRA/assignment-ii/-/merge_requests/17
For this merge request, we removed the methods marked for deprecation and used setters as recommended by the code base. We generated setters for the entity functions position variables and placed code in bomb, logical bomb and gameMap that used these setters while also letting the tests pass them. 

Merge Request 3:
https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/M11C_ABRA/assignment-ii/-/merge_requests/18

When testing with Bomb and LogicalBomb tests, we discovered when a Boulder preceded the player when pushed on top of a Bomb, the Bomb loses functionality.
The Bomb’s state gets shifted to INVENTORY regardless of the type of object it intersects with.To fix this the line ‘this.state = State.INVENTORY inside the if statement to check that the Entity overlapping the Bomb is a Player. This will ensure the Bomb’s state will only change to INVENTORY when it overlaps with an Entity that is Player. 

Merge Request 4:
https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/M11C_ABRA/assignment-ii/-/merge_requests/19
While testing the 2_doors dungeon it was discovered that it did not perform as the specification required it to: “The player can only carry one key at a time”, however the player was able to pick up multiple keys. 
Hence, assuming keys can still be moved onto even if they cannot be picked up, this issue was fixed by adding an additional if statement to Key that counts the number of keys in the Players inventory using the .count method and returns early if the count != 0.
The test “cannotPickUpTwoKeys” was also adjusted accordingly, now asserting that there is only 1 key in the players inventory.
