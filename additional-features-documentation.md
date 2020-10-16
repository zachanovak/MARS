Additional Features Documentation
=================================

The additional features include the Phobos Menu (with 4 new features)
and the Auto Layout feature which is accessible directly from the main
GUI.

-   Phobos Menu
    -----------

    To access the Phobos Menu in MARS go to **Tools** -\> **Phobos
    Menu**

    1.  ### Comma Constraint

        Tick the checkbox next to Comma Constraint in the Phobos menu to
        enable this feature. When enabled, this feature strictly
        requires commas to be used in instructions and arrays for the
        program to assemble.

        Example usage:

              addi $t1 $t0 1    # This line will NOT assemble because of the lack of commas
              addi $t1, $t0, 1  # This line WILL assemble because it has the correct number of commas

        Note that if the Comma Constraint feature were to be disabled,
        the above two lines would both assemble.

    2.  ### Register Name Constraint

        Tick the checkbox next to Register Name Constraint in the Phobos
        menu to enable this feature. When enabled, this feature strictly
        requires register names to be used, rather than register
        numbers, for the program to assemble.

        Example usage:

              addi $1, $0, 1    # This line will NOT assemble because register numbers are being used
              addi $t1, $t0, 1  # This line WILL assemble because register names are being used

        Note that if the Register Name Constraint feature were to be
        disabled, the above two lines would both assemble (albeit, the
        first line would then be using possibly different registers than
        intended).

    3.  ### Instruction Subset

    4.  ### Show Register Usage

-   ### Auto Layout Feature

    There are three ways one can use the Auto Layout feature in MARS
    while editing a file:

    -   Go to **Edit** -\> **Auto Layout**
    -   Press the ![](images/AutoLayout22.png)Auto Layout icon in the
        icon menu below the top menu bar
    -   Press **Ctrl** + **Q**

