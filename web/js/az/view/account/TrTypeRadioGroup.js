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
                    boxLabel:'����� �� �����',
                    name:'type',
                    inputValue:'p',
                    margin:'0 5 0 0'
                },
                {
                    xtype:'radio',
                    boxLabel:'����� �� �����',
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
                    boxLabel:'����� � �����',
                    name:'type',
                    inputValue:'i',
                    margin:'0 5 0 0'
                },
                {
                    xtype:'radio',
                    boxLabel:'���������� �� �����',
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
