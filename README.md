# Palindrome line Validator

This is a small program to verify and count the number of valid input lines based on criteria for each line:

- Contains at least one  palindrome constructed by 4 non duplicated characters, which does not appear inside square bracket "[]"

Due to the ambiguity of the requirements, there are some assumptions made that:

- For any characters outside of a closing bracket, considering any bracket character (including empty space) as valid char to verify the palindrome pattern. eg: $aa$ is considered a valid pattern
- Only the most inner characters in a nested bracket would be considered as chars in a bracket. eg: "ab[cddc[abc]]asd" is considered as valid pattern because "cddc" is considered as characters outside of bracket
- For non closing bracket, consider as characters outside of bracket. eg: "abc]deffe[" is considered as valid pattern because effe is considered as not inside a bracket.

## Build and run the program

Under the project root folder, run command
```
./gradlew build
```
Execute the built jar file under build folder with the input file containing the lines to verify
```aidl
java -jar build/libs/PalindromeValidator-${version}-SNAPSHOT.jar ${path_to_the_input_file}
```
The result containing the number of valid lines and all valid lines content would be in an output file under the same folder
