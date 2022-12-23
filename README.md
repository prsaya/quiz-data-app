# Quiz Data App

This is a Java Swing GUI app to perform CRUD (Create, Read, Update, Delete) operations on quiz data in a JSON file.

The JSON file was used in another app for rendering a Java quiz app's view. A sample JSON with a quiz data item and the browser view's picture of the rendered quiz data item is shown below. The Swing app was used to create the data, and is just an adhoc program.

## Data Sample:

```
  {
    "id": 15,
    "question": "Which of the following are true:",
    "type": "M",
    "options": [
      "(a) A private variable cannot be inherited by a subclass.",
      "(b) A class can extend only one abstract class.",
      "(c) Variables in an abstract class are defined using the <code>abstract</code> keyword.",
      "(d) An Interface defines abstract methods and constants."
    ],
    "answers": [
      "a",
      "b",
      "d"
    ],
    "notes": [
      "a, b and d are correct.",
      "c is incorrect.",
      "The <code>abstract</code> keyword cannot be used with variables."
    ]
  }
```

## The Quiz App's Screenshot:

![quiz app's rendered data](https://user-images.githubusercontent.com/38682661/209324066-39d5fd09-19b8-44b1-a247-d3461f537e1c.png)


