Issues faced:
Given sharedpreferences is Key|Value, assigning a default name fails as you can't
have more then 1, so we need to check for collisions before saving, and if there is
a collision update the name with a random number haha 