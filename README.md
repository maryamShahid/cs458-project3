# CS 458 Software Verification and Validation
## Project 3: Introduction to Test Driven Development (TDD)

## How To Install & Run The Web App

## Project setup
```
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Lints and fixes files
```
npm run lint
```

## How To Run The Tests
- Go to IntelliJ
- Open the Test Project (in the root directory, folder called "test")
- Perform Maven Clean&Install on test project but ensure 'skip tests' option is selected.
- Open test\src\test\TestClass.java file
- Change line 15 with the location of chromedriver.exe in your local (we used version 99 of chrome driver, yet, this version should be compatible with your Google Chrome)
- Run TestClass.java and tests should be running.
- To run the test individually, you can click on the run buttons next to test methods in TestClass.java

### Notes
1. Before you run the tests, do not forget the change line 15 in TestClass.java (we used version 99 of chrome driver, yet, this version should be compatible with your Google Chrome)
2. Make sure you are able to allocate port 8080. 
