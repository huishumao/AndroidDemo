package per.hsm.androiddemo.node

import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * (Read the Fucking source code)
 * @ProjectName:    AndroidDemo
 * @Package:        per.hsm.androiddemo.node
 * @ClassName:      ResportyTest
 * @Description:     java类作用描述
 * @Author:         Roid/hsm
 * @CreateDate:     2022/5/11 17:02
 */
class ResportyTest {

    lateinit var reporty: Resporty
    var noteList: MutableList<Note> = mutableListOf()

    @Before
    fun setUp(){
        reporty = Resporty()

        for (c in 'a'..'z'){
            noteList.add(Note(c.toString(), c.toString(), c.toInt()))
        }

    }

    @Test
    fun `test the list is shuffled`(){

        Assert.assertFalse(reporty.shuffleList(noteList)[0].name > reporty.shuffleList(noteList)[1].name)

    }

//    @Test
//    fun `test the list is order by asc`(){
//
//        reporty.orderByAsc()
//
//    }

}