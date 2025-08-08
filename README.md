
# 🏨 Hotel Reservation System (HRS)

A complete Java-based solution for hotel room management, booking, and payment processing with MySQL database integration and Swing GUI.

## 🌟 Features

### Core Functionality
- **Room Management**
    - 3 room types (Standard, Deluxe, Suite) with dynamic pricing
    - Real-time availability tracking
    - Database persistence
 
- **Booking System**
    - Date-range availability checking
    - Booking confirmation with unique IDs
    - Cancellation with refund calculation
    - Payment processing (Cash/Card)

- **User Interface**
    - Role-based access (Admin/Staff)
    - Dashboard with professional design
    - Interactive forms with validation
    - Real-time data refreshes
  
### Project Structure
- hrs/
- ├── src/
- │   ├── (main/java/org/example/hrc)
- │   │   ├──    (src)
- │   │   │       ├── Dashboard.java         # Welcome screen
- │   │   │       ├── MainMenu.java          # Primary interface
- │   │   │       ├── Hotel.java             # Core business logic
- │   │   │       ├── Room.java              # Room entity
- │   │   │       ├── Booking.java           # Booking entity
- │   │   │       ├── RoomDAO.java           # Database operations for rooms
- │   │   │       ├── BookingDAO.java        # Database operations for bookings
- │   │   │       ├── LoginSystem.java       # Authentication gateway
- │   │   │       └── DBUtil.java            # Database connection
- │   │   │ 
- │   │   └── resources/                 # Images and other assets
- ├── pom.xml                            # Maven configuration
- └── README.md 

### Technical Highlights
- **MVC Architecture**
- **MySQL Database Integration**
- **Swing GUI** with modern styling
- **Date/Input Validation**
- **Automated Confirmation Numbers**

## 🛠️ Technologies Used

- **Backend**: Java 24 
- **Database**: MySQL (JDBC)
- **GUI**: Java Swing
- **Build Tool**: intelliJ IDEA (Maven)
- **Version Control**: Git

## 🚀 Getting Started

### Prerequisites
- Java JDK 24+
- MySQL Server 5.7+
- Maven (for building)

### Installation
1. **Database Setup**:
   ```sql
   CREATE DATABASE hotel_reservation_system;
   USE hotel_reservation_system;
   
   CREATE TABLE rooms (
       room_number INT PRIMARY KEY,
       type ENUM('STANDARD', 'DELUXE', 'SUITE'),
       price_per_night DECIMAL(10,2),
       is_available BOOLEAN
   );
   
   CREATE TABLE bookings (
       booking_id VARCHAR(20) PRIMARY KEY,
       guest_name VARCHAR(100),
       room_number INT,
       check_in DATE,
       check_out DATE,
       status VARCHAR(20),
       payment_method VARCHAR(20),
       payment_date DATE,
       FOREIGN KEY (room_number) REFERENCES rooms(room_number)
   );
2.Configure Database:
- Update connection details in DBUtil.java:
- private static final String URL = "jdbc:mysql://localhost:3306/hotel_reservation_system";
- private static final String USER = "your_username";
- private static final String PASSWORD = "your_password";
### Run the Application:
- Download the zip file from repository.
- Configure the Database with mySQL.
- Navigate the source code(***HRS/src/main/java/org/example/hrs/Login System.java****);
- Run the (***LoginSystem.java***) by intelliJ IDEA.

### System Architecture
  ![System Architecture](Screenshot/Diagram.png)  
## System Overview
### Login Page
![System Overview](Screenshot/Login.png)  

### Dashboard
![System Overview](Screenshot/Dashboard.png) 

### MainMenu
![System Overview](Screenshot/MainMenu.png)

### View All Available Room
![System Overview](Screenshot/AllRoom.png)

### BookingRoom
![System Overview](Screenshot/BookingRoom.png)
### ProcessPayment
![System Overview](Screenshot/processPayment.png)
### View All Booking
![System Overview](Screenshot/AllBooking.png)
### Cancel Reservation 
![System Overview](Screenshot/cancel.png)







# codealpha_hotel_Reservation_System
