Wedding Planner 4 U (WP4U)

An Android mobile application that helps wedding planners organize their inspiration in one place. Instead of scattering ideas across screenshots, saved links, and photo galleries, planners can sort images into clear, wedding-related vision board categories.

About the Project

WP4U provides pre-made categories such as Wedding Dress Inspo, Food, and Churches, and lets users upload their own images into each one. Users can replace or delete images at any time to keep their boards current. Each user has their own account, and all account details and images are stored locally in a Room (SQLite) database on the device.

This project was built as part of CST8319 - Software Development Project using an Agile Scrum workflow.

Features
User sign-up and sign-in to save data across sessions
Pre-made vision board categories (Wedding Dresses, Cakes/Desserts, Churches/Venues, Flowers, Food, Dress Code, and more)
Upload, replace, and delete images within each category
Drag-and-drop reordering of images
Local database storage for account information and images
Unit and integration tests to validate core functionality
Tech Stack
Tool	Purpose
Android Studio	Primary IDE for building, running, and testing the app
Kotlin	Primary programming language
XML	Layout and UI definitions
Room (SQLite)	Local database persistence layer for accounts, categories, and images
JUnit	Unit and integration testing framework
GitHub	Version control and team collaboration
Docker	Containerized development and deployment environment
Discord	Team coordination and Agile Scrum communication
Architecture

The app follows a repository pattern. UI components talk to a BoardRepository, which is backed by a Room database implementation (RoomBoardRepository). A ServiceLocator provides the repository instance, making it easy to swap implementations (for example, using fake data during early development before the database was wired in).

Database Structure

The app uses three core entities:

USER - stores account credentials (user_id, username, email, password_hash, created_at)
CATEGORY - holds pre-made vision board categories (category_id, category_name, description)
IMAGE - links users and categories to uploaded images (image_id, user_id, category_id, file_path, display_order, uploaded_at)

A user can upload many images (one-to-many), and a category can contain many images (one-to-many).

Getting Started
Prerequisites
Android Studio (latest version)
Android SDK (bundled with Android Studio)
Git
Installation
Clone the repository:
bash
   git clone https://github.com/aleblanc123/WP4U.git
Open the project in Android Studio.
Let Gradle sync and download dependencies.
Run the app on an emulator (AVD) or a connected Android device.
Running Tests

Run the unit and integration tests from Android Studio, or via the command line:

bash
./gradlew test
Branching

Each team member works on their own branch for their portion, then opens a pull request for review before merging into main. Example branches include database_functionality for the Room database work and feature branches for categories and image upload.

Team
Name	Contribution
Alex LeBlanc	Sign in/up page, GitHub repo setup
Maliah Stiles	Database (Room DAO) and documentation
R. Shema Yvan	Categories and image upload functionality
Reggie B.	README and project documentation
Mohamed (Wangdong)	Sample images / data population
Project Status

In developmet: initial release focuses on core functionality. Future plans include user-created categories and expanded features once the base version is complete.

License

This project is developed for educational purposes as part of CST8319 at Algonquin College.
