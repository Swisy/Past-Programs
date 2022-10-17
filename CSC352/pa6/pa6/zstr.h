typedef char* zstr;
typedef int zstr_code;

#define ZSTR_OK 0
#define ZSTR_ERROR 100
#define ZSTR_GREATER 1
#define ZSTR_LESS -1
#define ZSTR_EQUAL 0

zstr_code zstr_status;


zstr zstr_create(char* initial_data);

void zstr_destroy(zstr to_destroy);

void zstr_append(zstr* base, zstr to_append);

int zstr_index(zstr base, zstr to_search);

int zstr_count(zstr base, zstr to_search);

int zstr_compare(zstr x, zstr y);

zstr zstr_substring(zstr base, int begin, int end);

void zstr_print_detailed(zstr data);
