import tkinter as tk
import pandas as pd
import pymysql
# Establish a connection to the MySQL database
con = pymysql.connect(host='localhost', port=3306, user='root', password='Spandan@12', database='dbms')
# Create a cursor object to execute SQL statements
cursor = con.cursor()
# Create the table if it doesn't exist
create_table_query = """CREATE TABLE IF NOT EXISTS details (Stud_id INT PRIMARY KEY, Name VARCHAR(255), Branch VARCHAR(255), Date_of_Birth DATE, College_Name VARCHAR(255), Phone_Number VARCHAR(255))"""
cursor.execute(create_table_query)
# Commit the changes to the database
con.commit()
# Create a Tkinter window
window = tk.Tk()
window.title("Database Operations")
# Function to execute the selected action
def perform_action():
    # Get the selected action from the dropdown menu
    action = action_var.get()
    if action == "display":
        # Execute the SELECT statement
        cursor.execute("SELECT * FROM details")
        # Fetch all rows from the result
        rows = cursor.fetchall()
        # Create a Pandas DataFrame from the fetched data
        df = pd.DataFrame(rows, columns=[desc[0] for desc in cursor.description])
        # Create a new window to display the DataFrame
        new_window = tk.Toplevel(window)
        new_window.title("Data")
        # Create a Tkinter label for DataFrame column names
        col_labels = tk.Label(new_window, text="  |  ".join(df.columns), font=("Arial", 30, "bold"))
        col_labels.pack()
        # Iterate over DataFrame rows and create Tkinter labels for each row
        for index, row in df.iterrows():
            row_label = tk.Label(new_window, text="  |  ".join(str(cell) for cell in row), font=("Arial", 20))
            row_label.pack()
    elif action == "insert":
        # Prompt the user for values to insert
        stud_id = stud_id_entry.get()
        name = name_entry.get()
        branch = branch_entry.get()
        date_of_birth = date_of_birth_entry.get()
        college_name = college_name_entry.get()
        phone_number = phone_number_entry.get()
        # Define the INSERT query
        insert_query = "INSERT INTO details (Stud_id, Name, Branch, Date_of_Birth, College_Name, Phone_Number) VALUES (%s, %s, %s, %s, %s, %s)"
        # Create a tuple with the user-provided values
        values = (stud_id, name, branch, date_of_birth, college_name, phone_number)
        # Execute the INSERT statement
        cursor.execute(insert_query, values)
        # Commit the changes to the database
        con.commit()
    elif action == "update":
        # Prompt the user for the column and value to update
        column = update_column_entry.get()
        new_value = update_value_entry.get()
    # Prompt the user for the condition to update data
        condition = update_condition_entry.get()
        # Define the UPDATE query
        update_query = f"UPDATE details SET {column} = %s WHERE {condition}"
        # Execute the UPDATE statement
        cursor.execute(update_query, (new_value,))
        # Commit the changes to the database
        con.commit()
    elif action == "delete":
        # Prompt the user for the condition to delete the data
        condition = delete_condition_entry.get()
        # Define the DELETE query
        delete_query = f"DELETE FROM details WHERE {condition}"
        # Execute the DELETE statement
        cursor.execute(delete_query)
        # Commit the changes to the database
        con.commit()
    else:
        print("Invalid action selected.")
# Create a Tkinter dropdown menu for selecting the action
action_var = tk.StringVar(window)
action_var.set("select")  # Default action is "select"
action_label = tk.Label(window, text="Select action:")
action_label.pack()
action_dropdown = tk.OptionMenu(window, action_var, "display", "insert", "update", "delete")
action_dropdown.pack()
# Create input fields and labels for insert action
stud_id_label = tk.Label(window, text="Stud_id:")
stud_id_label.pack()
stud_id_entry = tk.Entry(window)
stud_id_entry.pack()
name_label = tk.Label(window, text="Name:")
name_label.pack()
name_entry = tk.Entry(window)
name_entry.pack()
branch_label = tk.Label(window, text="Branch:")
branch_label.pack()
branch_entry = tk.Entry(window)
branch_entry.pack()
date_of_birth_label = tk.Label(window, text="Date of Birth:")
date_of_birth_label.pack()
date_of_birth_entry = tk.Entry(window)
date_of_birth_entry.pack()
college_name_label = tk.Label(window, text="College Name:")
college_name_label.pack()
college_name_entry = tk.Entry(window)
college_name_entry.pack()
phone_number_label = tk.Label(window, text="Phone Number:")
phone_number_label.pack()
phone_number_entry = tk.Entry(window)
phone_number_entry.pack()
# Create input fields and labels for update action
update_column_label = tk.Label(window, text="Column to update:")
update_column_label.pack()
update_column_entry = tk.Entry(window)
update_column_entry.pack()
update_value_label = tk.Label(window, text="New value:")
update_value_label.pack()
update_value_entry = tk.Entry(window)
update_value_entry.pack()
update_condition_label = tk.Label(window, text="Condition:")
update_condition_label.pack()
update_condition_entry = tk.Entry(window)
update_condition_entry.pack()
# Create input fields and labels for delete action
delete_condition_label = tk.Label(window, text="Condition to delete:")
delete_condition_label.pack()
delete_condition_entry = tk.Entry(window)
delete_condition_entry.pack()
# Create a button to perform the selected action
action_button = tk.Button(window, text="Perform Action", command=perform_action)
action_button.pack()
# Run the Tkinter event loop
window.mainloop()
# Close the cursor and database connection
cursor.close()
con.close()