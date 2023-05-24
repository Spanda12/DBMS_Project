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

# Prompt the user for the column and value to update
column = input("Enter the column to update: ")
new_value = input("Enter the new value: ")

# Prompt the user for the condition to update data
condition = input("Enter the condition to update data: ")

# Define the UPDATE query
update_query = f"UPDATE details SET {column} = %s WHERE {condition}"

# Execute the UPDATE statement
cursor.execute(update_query, (new_value,))

# Commit the changes to the database
con.commit()

# Close the cursor and database connection
cursor.close()
con.close()
