# Contributing

The plugin is currently not accepting additions beyond the following cases (due to an impending backend overhaul):
1. A bugfix from a known issue or implementation of an enhancement requested for an existing feature
2. An optimization of existing code

If you would like to contribute code to the plugin, do not push directly to the master branch.
Instead, create a new fork and a pull request. Once the functionality of your contribution is verified and conformity to styling and constraints are verified, it will be merged to master. This merge may only be completed by 14er.

Please maintain the same styling as is already present in the code. This includes things like
- 4-space tabs in class files
- Spaces between the name and parameter in function definitions
- No space when calling a function in code before paranthesis
- Opening braces on the same line

Other things to note:
- Do not include any external frameworks (exception Spigot API)
- If a variable is slow to instantate (e.g. World); declare it in GlobalVars and instantiate in Main
- If a variable is declared in a higher context than your function, use it instead
- Make as few calls to the Spigot API as possible (this helps with cross-version porting and updating)
- Never use a depreciated method
- Try to optimize your code
