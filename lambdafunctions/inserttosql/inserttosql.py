import sys
import logging
import rds_config
import pymysql
import json

rds_host  = rds_config.db_endpoint
name = rds_config.db_username
password = rds_config.db_password
db_name = rds_config.db_name
port = 3306
result = "" 

try: 
    conn = pymysql.connect(rds_host, user=name,passwd=password, db=db_name, connect_timeout=5)

except: 
    result += "Could not connect to db"

def lambda_handler(event, context):
    
    temp = event["temp"]
    id = event["id"]
    type = event["type"]
    time = event["time"]
    
    query = "INSERT INTO data VALUES ('" + type+"','"+ str(temp)+"','" +time+"','" +id+"')";
    
    with conn.cursor() as cur:
        cur.execute(query)
        result=cur.fetchone()
    
    conn.commit()
    return result
