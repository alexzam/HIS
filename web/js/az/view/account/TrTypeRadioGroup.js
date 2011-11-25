Ext.define('alexzam.his.view.account.TrTypeRadioGroup', {
    extend:'Ext.form.RadioGroup',
    alias:'widget.alexzam.account.TrTypeRadioGroup',

    layout:'vbox',

    allowBlank:false,
    listeners:{
        change:this.onChange
    },
    items:[
        {
            xtype:'panel',
            layout:'hbox',
            border:0,
            width:250,
            items:[
                {
                    xtype:'radio',
                    boxLabel:'Трата из своих',
                    name:'type',
                    inputValue:'p',
                    margin:'0 5 0 0'
                },
                {
                    xtype:'radio',
                    boxLabel:'Трата из Казны',
                    name:'type',
                    inputValue:'a'
                }
            ]
        },
        {
            xtype:'panel',
            layout:'hbox',
            border:0,
            width:250,
            items:[
                {
                    xtype:'radio',
                    boxLabel:'Вклад в Казну',
                    name:'type',
                    inputValue:'i',
                    margin:'0 5 0 0'
                },
                {
                    xtype:'radio',
                    boxLabel:'Возмещение из Казны',
                    name:'type',
                    inputValue:'r'
                }
            ]
        }
    ],

    initComponent:function(){
        this.callParent();
        this.addEvents('typechanged');
//        this.on('change', this.onChange);
    },

    onChange:function (me, val)
    {
        var type = val.type;
        this.fireEvent('typechanged', type);
    }
});
