# Chess Project

A Java-based chess game implementation with a clean, modular architecture.

## Project Structure

```
ChessProject/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── chess/
│   │   │           ├── pieces/     # Chess piece implementations
│   │   │           ├── engine/     # Game logic and move validation
│   │   │           ├── board/      # Board representation and management
│   │   │           ├── game/       # Game controller and flow
│   │   │           ├── utils/      # Utility classes
│   │   │           └── ui/         # User interface components
│   │   └── resources/              # Resource files
│   └── test/
│       └── java/                   # Test classes
├── pom.xml                         # Maven configuration
└── README.md                       # This file
```

## Features (To Be Implemented)

- Complete chess piece movement validation
- Chess board representation and display
- Game state management
- Move history tracking
- Check and checkmate detection
- Special moves (castling, en passant, promotion)
- User interface (console-based initially)

## Requirements

- Java 17 or higher
- Maven 3.6 or higher

## Building and Running

### Build the project

```bash
mvn clean compile
```

### Run tests

```bash
mvn test
```

### Package the application

```bash
mvn package
```

### Run the application

```bash
mvn exec:java -Dexec.mainClass="com.chess.ChessGame"
```

## Development Guidelines

### Package Organization

- **pieces/**: Contains all chess piece classes (Pawn, Rook, Knight, Bishop, Queen, King)
- **engine/**: Game logic, move validation, and AI components
- **board/**: Board representation, position management, and board utilities
- **game/**: Game controller, game state, and game flow management
- **utils/**: Utility classes and helper methods
- **ui/**: User interface components (console, GUI, etc.)

### Code Style

- Follow standard Java naming conventions
- Use meaningful variable and method names
- Add appropriate JavaDoc comments for public methods
- Keep methods focused and single-purpose
- Use appropriate design patterns where applicable

## Testing

The project uses JUnit 5 for testing. Test classes should mirror the main package structure in the `src/test/java` directory.

## Contributing

1. Follow the existing code structure and conventions
2. Add appropriate tests for new functionality
3. Ensure all tests pass before committing
4. Document public APIs with JavaDoc

## License

This project is for educational purposes.
