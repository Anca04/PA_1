#!/bin/bash

# Darius-Florentin Neatu <neatudarius@gmail.com>

# This script should not be run!
# Please source it at the end of your `check` script!

DEBUG=on
total=0                  # don't change
UTILS=_utils             # don't change
SOURCE_TMP_DIR=src_check # don't change
score_easy=(0 0)
score_medium=0
score_hard=0
easy_problems=("feribot" "stocks" "nostory" "walsh")
medium_problems=("badgpt" "prinel")
hard_problems=("crypto" "regele")

check_readme() {
    README=README

    echo "------------------------------------------------------------"
    echo "------------------------------------------------------------"
    echo "================>>>>>> Check $README <<<<<< ================"

    score=-5
    max_score=0

    if (($(echo "$total == 0" | bc -l))); then
        echo "Punctaj $README neacordat. Punctajul pe teste este $total!"
    elif [ ! -f $README ]; then
        echo "$README lipsa."
    elif [ -f $README ] && [ "$(ls -l $README | awk '{print $5}')" == "0" ]; then
        echo "$README gol."
    else
        score=$max_score
        echo "$README OK. Punctajul final se va acorda la corectare."
    fi

    total=$(bc <<<"$total + $score")

    echo "===============>>>>>> $README: $score/$max_score <<<<<< ==============="
    echo "-------------------------------------------------------------"
    echo "-------------------------------------------------------------"
}

check_cpp_errors() {
    cnt_cpp=$(cat $1 | grep "Total errors found" | cut -d ':' -f2 | cut -d ' ' -f2)
}

check_java_errors() {
    cnt_java=$(cat $1 | grep -E "(WARN)|(ERR)" | wc -l)
}

check_coding_style_full() {
    echo "-------------------------------------------------------------"
    echo "-------------------------------------------------------------"
    echo "===============>>>>>> Check coding style <<<<<< ==============="

    max_score=0
    score=-5

    if (($(echo "$total == 0" | bc -l))); then
        echo "Punctaj Coding style neacordat. Punctajul pe teste este $total!"
    else
        cpp_sources=$(find -L . \( -name '*.cpp' -o -name '*.c' -o -name '*.h' -o -name '*.hpp' \) ! \( -name 'Makefile_cpp' -o -name 'Makefile_c' \))
        java_sources=$(find -L . -name '*.java' ! -name 'Makefile.java')

        if [ ! -z "${cpp_sources}" ]; then
            check_cpp=$UTILS/coding_style/check_cpp.py

            ${check_cpp} ${cpp_sources} &>cpp.errors
            check_cpp_errors cpp.errors
        else
            echo "" >cpp.errors
            cnt_cpp=0
        fi

        if [ ! -z "${java_sources}" ]; then
            check_java=$UTILS/coding_style/check_java.jar
            java_errors=$UTILS/coding_style/java_errors.xml

            java -jar ${check_java} -c ${java_errors} ${java_sources} &>java.errors
            check_java_errors java.errors
        else
            echo "" >java.errors
            cnt_java=0
        fi

        if [ -z $ONLINE_JUDGE ]; then
            cat cpp.errors
            cat java.errors
        fi

        if (( cnt_cpp > 0 || cnt_java > 0 )); then
            echo "$cnt_cpp C/C++ errors found."
            echo "$cnt_java Java errors found."
        else
            score=$max_score
            echo "Coding style OK. Punctajul final se poate modifica la corectare."
        fi
    fi

    total=$(bc <<<"$total + $score")

    echo "===============>>>>>> Coding style: $score/$max_score <<<<<< ==============="
    echo "-------------------------------------------------------------"
    echo "-------------------------------------------------------------"
}

check_coding_style() {
    # Check if platform is online
    if [ -z $ONLINE_JUDGE ]; then
        check_coding_style_full
    else
        check_coding_style_dummy
    fi
}

timeout_test() {
    tag=$1
    timeout=$2

    (time timeout $timeout make -f $makefile $tag) &>error

    cnt=$(cat error | grep "'$tag' failed" | wc -l)

    if [ $cnt -gt 0 ]; then
        t=$(cat error | grep "real" | cut -d 'm' -f2 | cut -d 's' -f1 | tr ',' '.')
        if [ $(echo "$t > $timeout" | bc) -eq 1 ]; then
            rm -f error
            echo "$t s" >tle
        fi
    else
        t=$(cat error | grep "real" | cut -d 'm' -f2 | cut -d 's' -f1)
        echo "$t s" >output.time
        rm -f error
    fi
}

run_problem() {
    name=$1
    id=$2

    makefile=""
    if [ $(find -L . -name ${src_names[0]} | wc -l) -eq 1 ]; then
        makefile="Makefile_c"
    elif [ $(find -L . -name ${src_names[1]} | wc -l) -eq 1 ]; then
        makefile="Makefile_cpp"
    elif [ $(find -L . -name ${src_names[2]} | wc -l) -eq 1 ]; then
        makefile="Makefile_java"
    fi
    
    # Compile and check errors
    make -f $makefile p$id &>out.make
    cnt=$(cat out.make | grep failed | wc -l)

    echo "------------------------------------------------------------"
    echo "------------------------------------------------------------"
    echo "---------------------- Problema $id: $name -----------------"

    score=0

    if [ $(find -L . -name ${src_names[0]} -o -name ${src_names[1]} -o -name ${src_names[2]} | wc -l) -gt 1 ]; then
        echo "Surse multiple pentru problema $name! Trimite doar una!"
        echo "Numele sursei care contine functia main trebuie sa fie:"
        echo "${src_names[0]}, ${src_names[1]} sau ${src_names[2]}"
        echo "=============>>>>>> Scor : $score/$pmax <<<<<< ============="
        echo "------------------------------------------------------------"
        echo "------------------------------------------------------------"
        return
    elif [ $(find -L . -name ${src_names[0]} | wc -l) -eq 1 ]; then
        timeout=$(cat $UTILS/timeout/c.timeout$id)
        echo "---------------------- timp C => $timeout s -----------------"
    elif [ $(find -L . -name ${src_names[1]} | wc -l) -eq 1 ]; then
        timeout=$(cat $UTILS/timeout/c.timeout$id)
        echo "---------------------- timp C++ => $timeout s -----------------"
    elif [ $(find -L . -name ${src_names[2]} | wc -l) -eq 1 ]; then
        timeout=$(cat $UTILS/timeout/java.timeout$id)
        echo "---------------------- timp Java => $timeout s -----------------"
    else
        echo "Numele sursei care contine functia main trebuie sa fie:"
        echo "${src_names[0]}, ${src_names[1]} sau ${src_names[2]}"
        echo "=============>>>>>> Scor : $score/$pmax <<<<<< ============="
        echo "------------------------------------------------------------"
        echo "------------------------------------------------------------"
        return
    fi

    rm -rf $TESTS_DIR/$name/out/
    mkdir -p $TESTS_DIR/$name/out/

    for i in "${tests[@]}"; do
        IN=$TESTS_DIR/$name/input/$i-$name.in
        REF=$TESTS_DIR/$name/ref/$i-$name.ref
        OUT=$TESTS_DIR/$name/out/$i-$name.out

        if [ ! -f $IN ]; then
            echo "Test $i problema $id .......... 0/${points[$i]} - $IN lipseste!"
            continue
        fi

        cp $IN $name.in
        cp $REF res.ok

        touch $name.out
        chmod 666 $name.out

        timeout_test run-p$id $timeout

        if [ -f error ]; then
            echo "Test $i problema $id .......... 0.0/${points[$i]} - Run time error!"
            # TODO
            # cat error
            echo "$(cat error)"
        elif [ -f tle ]; then
            echo "Test $i problema $id .......... 0.0/${points[$i]} - TLE - $(cat tle)!"
        else
            ./verif $name ${points[$i]}

            STATUS=$(cat output.verif)
            TIME=$(cat output.time)
            SCORE=$(cat score.verif)
            echo "Test $i problema $id .......... $SCORE/${points[$i]} - $STATUS - $TIME"
            score=$(bc <<<"$score + $SCORE")
        fi

        if [ ! -z $DEBUG ]; then
            cp $name.out $OUT
        fi

        rm -f $name.in $name.out res.ok score.verif output.verif output.time \
            error.time error.exec error expected.time tle time.err run.err sd run.out
    done

    if [[ "${name}" = "${BONUS_TASK}" ]]; then
        echo ""
        echo ""
        cat _utils/.suprise/yoda.ascii
        echo ""
        echo ""
        if (($(echo "$score == $pmax" | bc -l))); then
            if [ -z "$ONLINE_JUDGE" ]; then
                if [ $(which mpg123 2>&1 | wc -l) -eq 0 ]; then
                    echo "'mpg123' must you install. Checker again must run. Bonus points may you lose!"
                    echo "		e.g. sudo apt-get install mpg123"
                    echo "		e.g. ./check"
                else
                    mpg123 _utils/.suprise/yoda.mp3 &>/dev/null
                fi
            fi
        fi
    fi

    echo "=============>>>>>> Scor : $score/$pmax <<<<<< ============="
    echo "------------------------------------------------------------"
    echo "------------------------------------------------------------"

    if [[ " ${easy_problems[@]} " =~ " ${name} " ]]; then
        for i in 0 1; do
            if (( $(echo "$score > ${score_easy[$i]}" | bc -l) )); then
            score_easy[$i]=$score
            break
            fi
        done
    elif [[ " ${medium_problems[@]} " =~ " ${name} " ]]; then
        if (( $(echo "$score > $score_medium" | bc -l) )); then
            score_medium=$score
        fi
    elif [[ " ${hard_problems[@]} " =~ " ${name} " ]]; then
        if (( $(echo "$score > $score_hard" | bc -l) )); then
            score_hard=$score
        fi
    fi

    make -f $makefile clean &>/dev/null
}

if [[ "$1" = "h" || "$1" = "help" ]]; then
    echo "Usage:"
    echo "       ./check                 # run the entire homework"
    echo "       ./check task_id         # run only one problem (e.g. number or name)"
    echo "       ./check cs              # run only the coding style check"
    exit 0
fi

TESTS_DIR=public_tests
if [ ! -z $1 ]; then
    # Check if platform is online
    ONLINE_JUDGE=
    if [ $1 = "ONLINE_JUDGE" ]; then
        ONLINE_JUDGE="ONLINE_JUDGE=\"-D=ONLINE_JUDGE\""
        TESTS_DIR=tests
    else
        TEST_TO_RUN=$1
    fi
else
    TEST_TO_RUN="ALL"
fi

(g++ --version 2>/dev/null >tmp && cat tmp | head -1) || (echo "Please install 'g++' :p!" && exit 1)
(gcc --version 2>/dev/null >tmp && cat tmp | head -1) || (echo "Please install 'gcc' :p!" && exit 1)
javac -version || (echo "[Warning] Please install 'javac' if you're using Java :p." && exit 1)
python2.7 --version || (echo "Please install 'python2.7' :p!'" && exit 1)
python3 --version || (echo "Please install 'python3' :p!" && exit 1)
rm -f tmp

# cat out.make
rm -f out.make
cnt=${cnt:-0}
if [ $cnt -gt 0 ]; then
    echo "Erori de compilare. Verifica versiunea compilatorului. STOP"
    echo "=============>>>>>> Total: $total/$MAX_POINTS <<<<<< ============="
    exit
fi

# Compile checker
make -f Makefile.PA all $ONLINE_JUDGE &>/dev/null
rm -rf $SOURCE_TMP_DIR

# Display tests set
echo "---------------------- Run $TESTS_DIR -------------------"

# Run tests - change functions test_*
test_homework $TEST_TO_RUN

#Sum the total
total=$(bc <<<"${score_easy[0]} + ${score_easy[1]} + $score_medium + $score_hard")

# Check coding style & Readme
check_readme
check_coding_style_full

# Clean junk
make -f $makefile clean &>/dev/null
make -f Makefile.PA clean &>/dev/null
rm -rf tmp out.make



# Display result
echo "Erorile de coding style se gasesc in cpp.errors / java.errors"
echo "=============>>>>>> Total: $(echo "$total 0" | awk '{print ($1 > $2) ? $1 : $2}')/$MAX_POINTS <<<<<< ============="

# Play bonus sound
if (($(echo "$total == $MAX_POINTS" | bc -l))); then
    if [ -z "$ONLINE_JUDGE" ]; then
        if [ $(which mpg123 2>&1 | wc -l) -eq 0 ]; then
            echo "'mpg123' must you install. Checker again must run. Bonus points may you lose!"
            echo "			e.g. sudo apt-get install mpg123"
            echo "			e.g. ./check"
            exit 0
        fi
    fi

    echo ""
    echo "			PROUD OF YOU I AM!"
    echo ""

    if [ -z "$ONLINE_JUDGE" ]; then
        mpg123 _utils/.suprise/champions.mp3 &>/dev/null
    fi
fi
