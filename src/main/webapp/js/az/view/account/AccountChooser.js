Ext.define('alexzam.his.view.account.AccountChooser', {
    extend:'Ext.form.Panel',
    xtype:'az-accChooser',

    requires:[

    ],

    layout:'hbox',
    bodyPadding:5,

    items:[
        {
            xtype:'label',
            text:'Счёт:',
            style:{
                fontWeight:'bold'
            }
        }
    ]
});