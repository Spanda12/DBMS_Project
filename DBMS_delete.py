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

# Define the DELETE query
delete_query = "DELETE FROM details WHERE condition"

# Prompt the user for the condition to delete the data
condition = input("Enter the condition to delete data: ")

# Construct the full query
delete_query = delete_query.replace("condition", condition)

# Execute the DELETE statement
cursor.execute(delete_query)

# Commit the changes to the database
con.commit()

# Close the cursor and database connection
cursor.close()
con.close()
