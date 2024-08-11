from MySQL_Connector import connect_to_db
import streamlit as st
import openai
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
        if 'questions' not in st.session_state:
            st.session_state['questions'] = None

        if PET.st.session_state['questions'] is None:
            st.warning("먼저 문제를 생성하세요.")
        else:
            st.write(PET.st.session_state['questions'])

        title = st.text_input("제목을 입력하세요.")
        text = st.text_area("텍스트를 입력하세요.", height=200)

        col_create, col_save = st.columns(2)


        if col_create.checkbox("문제 생성"):
            if not text:
                st.error("text를 입력하세요!")
            else:
                st.write("서술형의 문제를 생성합니다.")
                num_questions = st.number_input("생성할 문제 수를 입력하세요 :", min_value=1, max_value=10, value=3)

                api_key = ''

                openai.api_key = api_key


                if 're_questions' not in st.session_state:
                    st.session_state['re_questions'] = ""


                if st.button("문제 생성"):
                    st.session_state['re_questions'] = generate_questions_reexam(text, num_questions)
                    question_parts = st.session_state['re_questions'].replace("*", "\\*").split('Ans:')
                    st.session_state['Q'] = question_parts[0]
                    st.session_state['A'] = question_parts[1]
                    st.success("문제가 생성되었습니다.")
                    query = "UPDATE membership SET question_current = question_current + %s WHERE user_id = %s"
                    cursor.execute(query, (num_questions, input_id,))
                    connection.commit()
                col_que, col_ans = st.columns(2)

                if col_que.button("질문 출력"):
                    if 'Q' in st.session_state:
                        st.write("질문:\n", st.session_state['Q'])
                    else:
                        st.warning("문제가 생성되지 않았습니다. 먼저 '문제 생성' 버튼을 눌러 질문을 생성하세요.")

                if col_ans.button("답 출력"):
                    if 'A' in st.session_state:
                        st.write("답:", st.session_state['A'])
                    else:
                        st.warning("문제가 생성되지 않았습니다. 먼저 '문제 생성' 버튼을 눌러 질문을 생성하세요.")

        if col_save.button("저장"):
            if not text or not title:
                st.error("제목 혹은 텍스트를 입력해주세요!")
            else:
                create_note(input_id, title, text)
                st.write("오답노트가 저장되었습니다.")

    else:
        st.error("아이디 확인을 먼저 해주세요.")
    return result

def generate_questions_reexam(text, num_questions):


    gpt_prompt = f"다음과 같이 {num_questions}개의 서술형 문제를 생성해주세요. 문제의 답은 반드시 중간에 보여지면 안되고 문제의 답은 무조건 마지막 질문 생성 후에 'Ans:'뒤에 모아서 한번에 출력해주세요."
    role_content = "You are a helpful assistant that generates open-ended questions based on the provided text."

    prompt = [
        {"role": "system", "content": role_content},
        {"role": "user", "content": text},
        {"role": "assistant", "content": gpt_prompt},
    ]

    response = openai.ChatCompletion.create(
        model="gpt-3.5-turbo",
        messages=prompt,
        temperature=0.5,
        max_tokens=2000,
        top_p=1,
        frequency_penalty=0,
        presence_penalty=0
    )

    answer = response['choices'][0]['message']['content'].strip()

    return answer

def create_note(input_id, title, contexts):
    query = "SELECT MAX(summary_num) FROM reexam WHERE user_id = %s"
    cursor.execute(query, (input_id,))
    result = cursor.fetchone()
    if result[0] is None:
        summary_num = 1
    else:
        summary_num = result[0] + 1

        # 입력된 값들을 데이터베이스에 저장
    query = "INSERT INTO reexam (user_id, summary_num, title, contexts) VALUES (%s, %s, %s, %s)"
    values = (input_id, summary_num, title, contexts)
    cursor.execute(query, values)
    connection.commit()

def main():
    st.title("P.E.T 오답노트")

    if 'id' not in st.session_state:
        st.session_state['id'] = None
    if PET.st.session_state['id'] is None:
        st.error("아이디 확인을 먼저 해주세요.")
    else:
        check_mypage(PET.st.session_state['id'])



if __name__ == "__main__":
    main()
