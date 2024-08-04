import mysql.connector
from mysql.connector import Error


def connect_to_db():
    try:
        connection = mysql.connector.connect(
            host='localhost',
            database='cabstone',
            user='root',
            password='jsjung7826~'
        )

        return connection

    except Error as e:
        print("데이터베이스 연결 오류:", e)
        return None
