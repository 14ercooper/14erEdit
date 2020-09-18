# Contributing

14erEdit is accepting pull requests adding new features, or ports to new platforms.

In addition to new features, the following also could do with some extra work:
1. A bugfix from a known issue or implementation of an enhancement requested for an existing feature
2. An optimization of existing code

If you would like to contribute code to the plugin, do not push directly to the master branch (this should be impossible either way).
Instead, create a new fork and a pull request. Once the functionality of your contribution is verified and conformity to styling and constraints are verified, it will be merged to master. This merge may only be completed by 14er.

Please maintain the same styling as is already present in the code. This includes things like
- 4-space tabs in class files
- Spaces between the name and parameter in function definitions
- No space when calling a function in code before paranthesis
- Opening braces on the same line
- A new line at the end of the file

Other things to note:
- Do not include any external frameworks (exception Spigot API, or the equivalent for the platform)
- If a variable is slow to instantate or needs to be used everywhere; declare it in GlobalVars and instantiate in Main
- If a variable is declared in a higher context than your function, use it instead
- Make as few calls to the Spigot API as possible (this helps with cross-version porting and updating)
- Never use a depreciated method
- Try to optimize your code
