import { IconSearch } from "@tabler/icons-react";
import { TextInput } from "@mantine/core";

interface SearchBarProps {
    value: string;
    placeholder?: string;
    onChange: (value: string) => void;
}

const SearchBar = ({
    value,
    placeholder,
    onChange,
}: SearchBarProps) => {
    return (
        <TextInput
            placeholder={placeholder}
            value={value}
            onChange={(e) => onChange(e.target.value)}
            leftSection={<IconSearch size={16} />}
            __clearable
        />
    );
};

export default SearchBar;