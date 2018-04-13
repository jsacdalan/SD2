/*************************************************************************/
/**
** @file        Task.c
** @defgroup    TSK
** @ingroup     TSK
**
** @brief Module Description - System Periodic Task functions:
**  Define functions for system periodic tasks.
**
**************************************************************
** @brief
** Services provided:
** - Task_Init():
**   - Initialize the System Periodic Task timers.
**
** - System_Task():
**   - Manage System Periodic Tasks.
**
** - System_Task_01():
**   - Periodic Task 1 called by the System.
**
** - System_Task_02():
**   - Periodic Task 2 called by the System.
**
** - System_Task_03():
**   - Periodic Task 3 called by the System.
**
***************************************************************************
**
** Authors:
**   @author    Benjamin D. Sweet
** @version     1.2
** @date
** - Creation Date: 04-May-2012
** - Edit Date:     25-May-2013
**
***************************************************************************
** @brief
** Revision History:
**
** Revision: 1.2    25-May-2013   B. Sweet
** - Update comment blocks for Doxygen.
**
** Revision: 1.1    02-Jun-2012   Benjamin Sweet
** - For TARGET_PC applications, added call to Get_KeyboardInput() before calling IO_Mgr_InputTask(),
**   and call to Set_ScreenOutput() after all periodic tasks have run.
**
** Revision: 1.0    04-May-2012   Benjamin Sweet
** - Original.
**
**************************************************************************/

/*
** Include Files
*/

   /* Configuration */
   #include "Task.h"      /* Includes Typedefs, public function prototypes and Definitions for configuration constants */

   /*
   ** Access to System components:
   */
   #include "..\app\mmc\MMC_api.h"
   #include "..\svc\tmr\Timer.h"
   #include "..\svc\io_mgr\IO_Mgr.h"

#if defined (TARGET_PC)
   #include "..\drv\drv_KeySim\KeySim.h"
 #if defined (DEBUG)
   #include "..\drv\drv_KeySim\dio\dio.h"
 #endif

#elif defined (TARGET_S12UB)
 #if defined (DEBUG)
   #include "..\drv\drv_MC9S12DG128\dio\dio.h"
 #endif
#endif


/*
** Function Prototypes
*/
void Task_Init(void);   /* Initialize the System Periodic Tasks */
void System_Task(void); /* Manage System Periodic Tasks         */

void System_Task_01(void);
void System_Task_02(void);
void System_Task_03(void);

/*
** Local Variables:
*/


/*
**  Module code begins:
*/

/*************************************************************************/
/**
** @ingroup         TSK
**
** @fn              Task_Init()
**
** @brief           Function Description:
**  - Initialize the System Periodic Task timers.
**
** @param [in]      void
**
** @param [out]     void
**
** @return          void
**
** @brief
** Usage:
**   Call to this function from should be in the format of
**    Task_Init();
**
***************************************************************************
** System Interface:
** -  System_Task_01() - 
** -  System_Task_02() - 
** -  System_Task_03() - 
**
**************************************************************************/
void Task_Init(void)
{
    /*
    ** Initialize System Periodic Tasks and Services:
    */
    /* Applications: */
    HMI_Init();                 /* Initialize Human-Machine Interface component */

    /* Services: */
    IO_Mgr_Init();              /* Initialize Input/Output Manager component    */


    /*
    ** Initialize Periodic Task Timers:
    */

    /*  Task 1: */
    (void)Set_TimerDuration(TASK_TIMER_01, TASK_TIMER_DURATION_01);
    (void)Start_Timer(TASK_TIMER_01);
    /*  Task 2: */
    (void)Set_TimerDuration(TASK_TIMER_02, TASK_TIMER_DURATION_02);
    (void)Start_Timer(TASK_TIMER_05ms);
    /* Task 3: */
    (void)Set_TimerDuration(TASK_TIMER_03, TASK_TIMER_DURATION_03);
    (void)Start_Timer(TASK_TIMER_03);

  #if defined(TARGET_PC)
    /*
    ** Display initial system status:
    */
    Set_ScreenOutput(); /* Display system status on the screen. */
  #endif /* TARGET_PC */
}


/*************************************************************************/
/**
** @ingroup         TSK
**
** @fn              System_Task()
**
** @brief           Function Description:
**  - Manages periodic system tasks.
**
** @param [in]      void
**
** @param [out]     void
**
** @return          void
**
** @brief
** Usage:
**   Call to this function from should be in the format of
**    System_Task();
**
***************************************************************************
** System Interface:
** -  System_Task_01() - 
** -  System_Task_02() - 
** -  System_Task_03() - 
**
**************************************************************************/
void System_Task(void)
{
      /*
      **  Task 1:
      */
   #if(0)
      if (Get_TimerStatus(TASK_TIMER_01) == TimerState_EXPIRED)
      { 
        (void)Restart_TimerPeriodicInterval(TASK_TIMER_01);
        System_Task_01();
      }
   #else
      System_Task_01();
   #endif

      /*
      **  Task 2:
      */
   #if(0)
      if (Get_TimerStatus(TASK_TIMER_02) == TimerState_EXPIRED)
      {
        (void)Restart_TimerPeriodicInterval(TASK_TIMER_02);
        System_Task_02();
      }
   #else
      System_Task_02();
   #endif

      /*
      ** Task 3:
      */
   #if(0)
      if (Get_TimerStatus(TASK_TIMER_03) == TimerState_EXPIRED)
      {
        (void)Restart_TimerPeriodicInterval(TASK_TIMER_03);
        System_Task_03();
      }
   #else
      System_Task_03();
   #endif
}


/*************************************************************************/
/**
** @ingroup         TSK
**
** @fn              System_Task_01()
**
** @brief           Function Description:
**  - Periodic Task 1 called by the System.
**
** @param [in]      void
**
** @param [out]     void
**
** @return          void
**
** @brief
** Usage:
**   Call to this function from should be in the format of
**    System_Task_01();
**
***************************************************************************
** System Interface:
** - IO_Mgr_InputTask()
**
**************************************************************************/
void System_Task_01(void)
{
  #if defined (TARGET_PC)
    Get_KeyboardInput();    /* Update simulated input signals from User Interface. */
  #endif /* TARGET_PC */

    IO_Mgr_InputTask();         /* Update Input Signals     */

    return;
}  /* End: System_Task_01() */


/*************************************************************************/
/**
** @ingroup         TSK
**
** @fn              System_Task_02()
**
** @brief           Function Description:
**  - Periodic Task 2 called by the System.
**
** @param [in]      void
**
** @param [out]     void
**
** @return          void
**
** @brief
** Usage:
**   Call to this function from should be in the format of
**    System_Task_02();
**
***************************************************************************
** System Interface:
** - HMI_Task()
** - IO_Mgr_OutputTask()
**
**************************************************************************/
void System_Task_02(void)
{
    HMI_Task();                 /* Human-MachineInterface application - periodic task  */

    IO_Mgr_OutputTask();        /* Update Output Signals */

    return;
}  /* End: System_Task_02() */


/*************************************************************************/
/**
** @ingroup         TSK
**
** @fn              System_Task_03()
**
** @brief           Function Description:
**  - Periodic Task 3 called by the System.
**
** @param [in]      void
**
** @param [out]     void
**
** @return          void
**
** @brief
** Usage:
**   Call to this function from should be in the format of
**    System_Task_03();
**
***************************************************************************
** System Interface:
** - ...()
**
**************************************************************************/
void System_Task_03(void)
{

    return;
}  /* End: System_Task_03() */

/*************************  End of file  **********************************/
