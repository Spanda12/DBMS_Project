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

# Define the SQL query to select data from the table
query = "SELECT * FROM details"

# Read the table into a Pandas DataFrame
df = pd.read_sql(query, con)

# Close the database connection
con.close()

# Display the DataFrame
print(df)
