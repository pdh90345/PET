from MySQL_Connector import connect_to_db
import streamlit as st
import pandas as pd
import PET

connection = connect_to_db()
cursor = connection.cursor()

def check_mypage(input_id):
    if input_id is None:
        return 0

    sql = "SELECT user_id FROM membership WHERE user_id = %s and login_check = 1;"
    cursor.execute(sql,(input_id, ))
    result = cursor.fetchall()
    if result:
        st.write("아이디가 확인 되었습니다.")
        st.subheader("작성한 오답노트 목록")
        fetch_notes(input_id)

        st.subheader("나의 진도율")
        study_check(input_id)

    else:
        st.error("아이디 확인을 먼저 해주세요.")
    return result


def fetch_notes(input_id):

    # 입력된 User ID 값과 일치하는 레코드를 데이터베이스에서 가져옴
    query = "SELECT summary_num as '노트 번호', title as '제목', contexts as '내용' FROM reexam WHERE user_id = %s"
    values = (input_id,)
    cursor.execute(query, values)
    results = cursor.fetchall()

    df = pd.DataFrame(results, columns=["노트번호", "제목", "내용"])


    # 결과 출력
    if len(results) > 0:
        st.dataframe(df)
    else:
        st.write("해당 User ID에 대한 노트가 없습니다.")


def study_check(input_id):

    if connection.is_connected():

        query = """select question_goal as '목표 문제 수', question_current as '생성 문제 수', (question_current / question_goal) * 100 as '진도율(%)' from membership where user_id = %s;"""

        cursor.execute(query, (input_id,))

        results = cursor.fetchall()

        df = pd.DataFrame(results, columns=["목표 문제 수", "생성 문제 수", "진도율(%)"])

        st.table(df)


def main():
    st.title("마이페이지")

    if 'id' not in st.session_state:
        st.session_state['id'] = None
    if PET.st.session_state['id'] is None:
        st.error("아이디 확인을 먼저 해주세요.")
    else:
        check_mypage(PET.st.session_state['id'])

if __name__ == "__main__":
    main()
