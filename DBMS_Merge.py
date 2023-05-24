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

while True:
    # Create a cursor object to execute SQL statements
    cursor = con.cursor()

    # Define the SQL query to select data from the table
    select_query = "SELECT * FROM details"

    # Prompt the user for the action to perform
    action = input("Enter the action to perform (select/insert/update/delete) or 'exit' to quit: ")

    if action == "select":
        # Execute the SELECT statement
        cursor.execute(select_query)

        # Fetch all rows from the result
        rows = cursor.fetchall()

        # Create a Pandas DataFrame from the fetched data
        df = pd.DataFrame(rows, columns=[desc[0] for desc in cursor.description])

        # Display the DataFrame
        print(df)

    elif action == "insert":
        # Prompt the user for values to insert
        stud_id = input("Enter Stud_id: ")
        name = input("Enter Name: ")
        branch = input("Enter Branch: ")
        year = input("Enter Year: ")
        college_name = input("Enter College Name: ")

        # Define the INSERT query
        insert_query = "INSERT INTO details (Stud_id, Name, Branch, Year, College_Name) VALUES (%s, %s, %s, %s, %s)"

        # Create a tuple with the user-provided values
        values = (stud_id, name, branch, year, college_name)

        # Execute the INSERT statement
        cursor.execute(insert_query, values)

        # Commit the changes to the database
        con.commit()

    elif action == "update":
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

    elif action == "delete":
        # Prompt the user for the condition to delete the data
        condition = input("Enter the condition to delete data: ")

        # Define the DELETE query
        delete_query = f"DELETE FROM details WHERE {condition}"

        # Execute the DELETE statement
        cursor.execute(delete_query)

        # Commit the changes to the database
        con.commit()

    elif action == "exit":
        # Close the cursor and database connection
        cursor.close()
        con.close()
        break

    else:
        print("Invalid action entered.")

    # Close the cursor
    cursor.close()

# Close the database connection
con.close()
