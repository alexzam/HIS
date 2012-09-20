Ext.define('alexzam.his.view.account.TrTypeRadioGroup', {
    extend:'Ext.form.RadioGroup',
    alias:'widget.alexzam.account.TrTypeRadioGroup',

    layout:'hbox',
    margin:'0 5 0 5',

    allowBlank:false,
    items:[
        {
            xtype:'panel',
            layout:'vbox',
            border:0,
            height:50,
            items:[
                {
                    xtype:'radio',
                    boxLabel:'Трата из своих',
                    name:'type',
                    inputValue:'p'
                },
                {
                    xtype:'radio',
                    boxLabel:'Вклад в Казну',
                    name:'type',
                    inputValue:'i',
                    margin:'3 0 0 0'
                }
            ]
        },
        {
            xtype:'panel',
            layout:'vbox',
            border:0,
            height:50,
            items:[
                {
                    xtype:'radio',
                    boxLabel:'Трата из Казны',
                    name:'type',
                    inputValue:'a'
                },
                {
                    xtype:'radio',
                    boxLabel:'Возмещение из Казны',
                    name:'type',
                    inputValue:'r',
                    margin:'3 0 0 0'
                }
            ]
        }
    ],

    initComponent:function(){
        var me = this;

        me.callParent();
        me.addEvents('typechanged');
    },

    onChange:function (newVal)
    {
        if(!Ext.isArray(newVal.type))
            this.fireEvent('typechanged', newVal.type);
    }
});