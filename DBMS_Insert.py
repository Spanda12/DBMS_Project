import pandas as pd
import pymysql

# Establish a connection to the MySQL database
con = pymysql.connect(
    host='localhost',
    port=3306,
    user='root',
    password='Spandan@12',
    database='dbms'
)

# Create a cursor object to execute SQL statements
cursor = con.cursor()

# Define the INSERT query
insert_query = "INSERT INTO details (Stud_id, Name, Branch, Year, College_Name) VALUES (%s, %s, %s, %s, %s)"

# Prompt the user for values
stud_id = input("Enter Stud_id: ")
name = input("Enter Name: ")
branch = input("Enter Branch: ")
year = input("Enter Year: ")
college_name = input("Enter College Name: ")

# Create a tuple with the user-provided values
values = (stud_id, name, branch, year, college_name)

# Execute the INSERT statement
cursor.execute(insert_query, values)

# Commit the changes to the database
con.commit()

# Close the cursor and database connection
cursor.close()
con.close()
